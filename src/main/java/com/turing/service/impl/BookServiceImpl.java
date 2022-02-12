package com.turing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.turing.common.ElasticsearchIndex;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.Book;
import com.turing.entity.BookBaseInfo;
import com.turing.entity.Tag;
import com.turing.entity.dto.BookDto;
import com.turing.entity.elasticsearch.BookDoc;
import com.turing.mapper.AddressMapper;
import com.turing.mapper.BookBaseInfoMapper;
import com.turing.mapper.BookMapper;
import com.turing.mapper.TagMapper;
import com.turing.service.BookService;
import com.turing.utils.HttpUtils;
import com.turing.utils.ISBNUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 16:05:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class BookServiceImpl implements BookService {

    String host = "https://jisuisbn.market.alicloudapi.com";
    String path = "/isbn/query";
    String method = "GET";
    @Value("${isbn.appcode}")
    String appcode;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookBaseInfoMapper baseInfoMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public Result uploadBookInfo (BookDto bookDto) {
        Integer addressId = bookDto.getAddressId();
        if (addressId == null || !addressMapper.selectById(addressId).getUserId().equals(bookDto.getUserId())) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("地址信息非法,上传失败!");
        }
        Book book = new Book();
        BookBaseInfo bookBaseInfo = (BookBaseInfo) getBookInfoByISBN(bookDto.getISBN()).getData();
        BeanUtils.copyProperties(bookBaseInfo, bookDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp createdTime = Timestamp.valueOf(simpleDateFormat.format(new Date()));
        book.transform(bookDto);
        book.setCreatedTime(createdTime);
        bookMapper.insert(book);
        //Elasticsearch用的Document实体
        BookDoc bookDoc = new BookDoc();
        bookDoc.transform(book);
        String json = JSON.toJSONString(bookDoc);
        log.info("ElasticSearch插入数据===>>>{}", json);
        IndexRequest request = new IndexRequest(ElasticsearchIndex.BOOK).id(bookDoc.getId().toString());
        request.source(json, XContentType.JSON);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("ElasticSearch插入数据===>>>{}失败!", json);
        }

        bookDto.setId(book.getId());
        bookDto.setCreatedTime(createdTime);
        return new Result().success(bookDto);
    }

    @Override
    public Result getBookInfo () {
        List<Book> books = bookMapper.selectList(new QueryWrapper<Book>().eq("status", 1));
        List<BookDto> bookDtoList = getBookDtoList(books);
        return new Result().success(bookDtoList);
    }

    @Override
    public Result getBookInfoByBookId (Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该书籍!");
        }
        BookDto bookDto = new BookDto();
        bookDto.transform(book);
        List<Tag> tagList = tagMapper.selectBatchIds(bookDto.getTagIdList());
        Map<String, Object> result = new HashMap<>();
        result.put("tagList", tagList);
        result.put("book", bookDto);
        return new Result().success(result);
    }

    @Override
    public Result getBookInfoByTag (Integer tag) {
        Integer selectCount = tagMapper.selectCount(new QueryWrapper<Tag>().eq("id", tag));
        if (selectCount.intValue() == 0) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该分类标签!");
        }
        List<Book> books = bookMapper.selectList(new QueryWrapper<Book>().likeLeft("tag_id", tag)
                .or()
                .likeRight("tag_id", tag)
                .or()
                .eq("tag_id", tag));
        List<BookDto> result = getBookDtoList(books);
        // 排除模糊搜索的非该分类的书籍
        result.removeIf(bookDto -> !bookDto.getTagIdList().contains(tag.toString()));
        if (result.size() == 0 || result == null) {
            return new Result().success("无返回数据!");
        }
        return new Result().success(result);
    }

    @Override
    public Result getBookInfoByISBN (String ISBN) {
        if (StringUtils.isBlank(ISBN)) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("ISBN编码为空,书籍查询失败!");
        }
        //ISBN编码的校验
        if (!ISBNUtils.checkIsbn(ISBN)) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("非法的ISBN编码,请重新扫描编码查询!");
        }
        //从Redis缓存中查找
        BookBaseInfo bookBaseInfo = (BookBaseInfo) redisTemplate.opsForValue().get(RedisKey.ISBN_KEY + ISBN);

        //从MySQL数据库中查找
        if (bookBaseInfo == null) {
            bookBaseInfo = baseInfoMapper.selectOne(new QueryWrapper<BookBaseInfo>().eq("isbn", ISBN));
        }
        //ISBN书号查询
        if (bookBaseInfo == null) {
            String result = queryBookByISBN(ISBN);
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getInteger("status").intValue() != 0) {
                log.info("ISBN[{}]查询失败,失败原因:[{}]", ISBN, jsonObject.getString("msg"));
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("ISBN查询失败,请确认后重试!");
            }
            bookBaseInfo = buildBookInfo(ISBN, jsonObject);
            //存入MySQL数据库
            baseInfoMapper.insert(bookBaseInfo);
        }

        //存入Redis缓存
        redisTemplate.opsForValue().setIfAbsent(RedisKey.ISBN_KEY + ISBN, bookBaseInfo);
        return new Result().success(bookBaseInfo);
    }

    private BookBaseInfo buildBookInfo (String ISBN, JSONObject jsonObject) {
        JSONObject resultObject = jsonObject.getJSONObject("result");
        BookBaseInfo bookBaseInfo = new BookBaseInfo();
        bookBaseInfo.setISBN(ISBN);
        bookBaseInfo.setTitle(resultObject.getString("title"));
        bookBaseInfo.setCover(resultObject.getString("pic"));
        bookBaseInfo.setSummary(resultObject.getString("summary"));
        bookBaseInfo.setPublisher(resultObject.getString("publisher"));
        bookBaseInfo.setPubdate(resultObject.getString("pubdate"));
        bookBaseInfo.setAuthor(resultObject.getString("author"));
        bookBaseInfo.setPrice(new BigDecimal(resultObject.getString("price")));
        bookBaseInfo.setBinding(resultObject.getString("binding"));
        return bookBaseInfo;
    }

    private String queryBookByISBN (String ISBN) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("isbn", ISBN);
        String result = null;
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            log.info("查询ISBN[{}]结果为:[{}]", ISBN, response.toString());
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<BookDto> getBookDtoList (List<Book> books) {
        List<BookDto> result = new ArrayList<>();
        for (Book book : books) {
            BookDto bookDto = new BookDto();
            bookDto.transform(book);
            result.add(bookDto);
        }
        return result;
    }
}
