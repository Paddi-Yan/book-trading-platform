package com.turing.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.ElasticsearchIndex;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.Book;
import com.turing.entity.User;
import com.turing.entity.dto.WechatUserInfo;
import com.turing.entity.dto.BookDto;
import com.turing.entity.dto.UserDto;
import com.turing.entity.elasticsearch.BookDoc;
import com.turing.interceptor.UserThreadLocal;
import com.turing.mapper.BookMapper;
import com.turing.mapper.UserMapper;
import com.turing.service.UserService;
import com.turing.service.WechatService;
import com.turing.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 01:26:10
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService
{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private RestHighLevelClient client;
    /*
    @Override
    public String getSessionId(String code)
    {
        //拼接URL：微信登录凭证校验接口
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        String replaceUrl = url.replace("APPID", appid).replace("SECRET", secret).replace("JSCODE", code);
        //发起http请求获取微信的返回结果
        String result = HttpUtil.get(replaceUrl);
        String uuid = UUID.randomUUID().toString();
        //存入redis
        redisTemplate.opsForValue().set(RedisKey.WX_SESSION_ID+uuid,result,30, TimeUnit.MINUTES);
        //生成sessionId返回至前端 作为当前登录用户的标识
        return uuid;
    }

    @Override
    public Result authLogin(WxAuthInfo wxAuthInfo)
    {
        //解密WxAuthInfo
        try {
            String json = wechatService.wechatDecrypt(wxAuthInfo.getEncryptedData(), wxAuthInfo.getSessionId(), wxAuthInfo.getIv());
            WechatUserInfo wechatUserInfo = JSON.parseObject(json, WechatUserInfo.class);
            String openId = wechatUserInfo.getOpenid();
            //解密后获取到用户信息-openId 性别 昵称 头像等信息
            //openId是唯一的 在user表中查询openId是否存在 存在-登录 不存在-注册
            //生成token令牌 返回给前端
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("open_id", openId).last("limit 1"));
            UserDto userDto = new UserDto();
            userDto.transform(wechatUserInfo);
            if (user == null)
            {
                //注册
                return this.registry(userDto);
            }else
            {
                //登录
                userDto.setId(user.getId());
                return this.login(userDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result().fail(HttpStatusCode.UNAUTHORIZED);
    }

    @Override
    public Result registry(UserDto userDto)
    {
        User user = new User();
        BeanListUtils.copyProperties(userDto,user);
        try {
            this.userMapper.insert(user);
            System.out.println("user"+user.getId());
            userDto.setId(user.getId());
            return this.login(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().fail(HttpStatusCode.ERROR);
        }
    }

    @Override
    public Result login(UserDto userDto)
    {
        String token = JWTUtils.sign(userDto.getId());
        userDto.setToken(token);
        userDto.setOpenid(null);
        redisTemplate.opsForValue().set(RedisKey.TOKEN+token,userDto,7,TimeUnit.DAYS);
        return new Result().success(userDto).message("用户登录成功!");
    }
     */

    @Override
    public Result getUserInfo(Boolean refreshToken)
    {
        UserDto userDto = UserThreadLocal.getUserInfoFromThread();
        if (refreshToken)
        {
            String token = JWTUtils.sign(userDto.getId());
            userDto.setToken(token);
            redisTemplate.opsForValue().set(RedisKey.TOKEN+token,userDto,7,TimeUnit.DAYS);
        }
        return new Result().success(userDto);
    }

    @Override
    public User getUserById(Integer userId)
    {
        return userMapper.selectById(userId);
    }

    @Override
    public Result getBookInfo(Integer userId, Integer type)
    {
        List<Book> books = bookMapper.selectList(new QueryWrapper<Book>()
                .eq("user_id", userId)
                .eq("type", type)
                .orderByDesc("created_time"));
        List<BookDto> bookDtoList = new ArrayList<>();
        for (Book book : books) {
            BookDto bookDto = new BookDto();
            bookDto.transform(book);
            bookDtoList.add(bookDto);
        }

        return new Result().success(bookDtoList);
    }

    @Override
    public Result updateBookInfo(BookDto bookDto)
    {
        Book book = new Book();
        book.transform(bookDto);
        bookMapper.updateById(book);
        BookDoc bookDoc = new BookDoc();
        bookDoc.transform(book);
        String json = JSON.toJSONString(bookDoc);
        UpdateRequest request = new UpdateRequest(ElasticsearchIndex.BOOK, String.valueOf(bookDoc.getId()));
        request.doc(json, XContentType.JSON);
        try {
            client.update(request, RequestOptions.DEFAULT);
            log.info("Elasticsearch修改书籍信息成功:{}",bookDoc);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Elasticsearch修改书籍信息失败:{}",bookDoc);
        }
        return new Result().success(bookDto);
    }

    /**
     * 下架图书
     * @return
     */
    @Override
    public Result withdrawBookInfo(Integer bookId, Integer userId)
    {
        Book book = bookMapper.selectOne(new QueryWrapper<Book>().eq("id", bookId).eq("user_id", userId));
        if (book == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("图书编号不存在或该用户无修改该图书权限!");
        }
        //将书籍标记为已失效
        book.setStatus(0);
        bookMapper.updateById(book);

        UpdateRequest request = new UpdateRequest(ElasticsearchIndex.BOOK, String.valueOf(book.getId()));
        request.doc(XContentType.JSON,"status",0);
        try {
            client.update(request, RequestOptions.DEFAULT);
            log.info("Elasticsearch修改编号为[{}]的书籍信息状态成功,目前状态为已下架",book.getId());
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Elasticsearch修改编号为[{}]的书籍信息失败:",book.getId());
        }
        return new Result().success(book);
    }

    /**
     * 删除已下架的图书信息
     * @return
     */
    @Override
    public Result deleteHistory(Integer bookId, Integer userId)
    {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>().eq("id", bookId).eq("user_id", userId);
        Book book = bookMapper.selectOne(queryWrapper);
        // 查询不到图书信息 || 书籍处于有效中
        if (book == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("图书编号不存在或该用户无修改该图书权限!");
        }
        if (book.getStatus().equals(1))
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("图书状态为有效,无法删除");
        }
        bookMapper.delete(queryWrapper);
        DeleteRequest request = new DeleteRequest(ElasticsearchIndex.BOOK, String.valueOf(book.getId()));
        try {
            client.delete(request,RequestOptions.DEFAULT);
            log.info("Elasticsearch删除书籍信息成功:{}",book);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Elasticsearch删除书籍信息失败:{}",book);
        }
        return new Result().success(book);
    }

    @Override
    public User getUserByOpenId(String openid)
    {
        return userMapper.selectOne(new QueryWrapper<User>().eq("openid",openid));
    }


}
