package com.turing.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.ElasticsearchIndex;
import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Book;
import com.turing.entity.Tag;
import com.turing.entity.dto.BookDto;
import com.turing.entity.elasticsearch.BookDoc;
import com.turing.mapper.BookMapper;
import com.turing.mapper.TagMapper;
import com.turing.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 16:05:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class BookServiceImpl implements BookService
{

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RestHighLevelClient client;
    @Override
    public Result uploadBookInfo(BookDto bookDto)
    {
        Book book = new Book();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp createdTime = Timestamp.valueOf(simpleDateFormat.format(new Date()));
        book.transform(bookDto);
        book.setCreatedTime(createdTime);
        bookMapper.insert(book);
        //Elasticsearch用的Document实体
        BookDoc bookDoc = new BookDoc();
        bookDoc.transform(book);
        String json = JSON.toJSONString(bookDoc);
        log.info("ElasticSearch插入数据===>>>{}",json);
        IndexRequest request = new IndexRequest(ElasticsearchIndex.BOOK).id(bookDoc.getId().toString());
        request.source(json, XContentType.JSON);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("ElasticSearch插入数据===>>>{}失败!",json);
        }

        bookDto.setId(book.getId());
        bookDto.setCreatedTime(createdTime);
        return new Result().success(bookDto);
    }

    @Override
    public Result getBookInfo(Integer type)
    {
        List<Book> books = bookMapper.selectList(new QueryWrapper<Book>().eq("type", type).eq("status",1));
        List<BookDto> bookDtoList = getBookDtoList(books);
        return new Result().success(bookDtoList);
    }

    @Override
    public Result getBookInfoByBookId(Integer bookId)
    {
        Book book = bookMapper.selectById(bookId);
        if (book == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该书籍!");
        }
        BookDto bookDto = new BookDto();
        bookDto.transform(book);
        List<Tag> tagList = tagMapper.selectBatchIds(bookDto.getTagIdList());
        Map<String,Object> result = new HashMap<>();
        result.put("tagList",tagList);
        result.put("book",bookDto);
        return new Result().success(result);
    }

    @Override
    public Result getBookInfoByTag(Integer tag)
    {
        List<Book> books = bookMapper.selectList(new QueryWrapper<Book>().likeLeft("tag_id", tag)
                .or()
                .likeRight("tag_id", tag)
                .or()
                .like("tag_id", tag));
        List<BookDto> result = getBookDtoList(books);
        return new Result().success(result);
    }

    private List<BookDto> getBookDtoList(List<Book> books)
    {
        List<BookDto> result = new ArrayList<>();
        for (Book book : books) {
            BookDto bookDto = new BookDto();
            bookDto.transform(book);
            result.add(bookDto);
        }
        return result;
    }
}
