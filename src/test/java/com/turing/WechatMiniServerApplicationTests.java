package com.turing;

import com.alibaba.fastjson.JSON;
import com.turing.common.ElasticsearchIndex;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.User;
import com.turing.entity.dto.ActivityDto;
import com.turing.entity.dto.BookDto;
import com.turing.entity.dto.UserDto;
import com.turing.entity.elasticsearch.RequestParams;
import com.turing.mapper.BookMapper;
import com.turing.service.*;
import com.turing.service.impl.BookServiceImpl;
import com.turing.service.impl.WechatServiceImpl;
import com.turing.utils.FTPUtils;
import com.turing.utils.JWTUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest(classes = WechatMiniServerApplication.class)
class WechatMiniServerApplicationTests
{

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private TagService tagService;

    @Test
    void testA()
    {
        System.out.println(bookService);
        System.out.println(bookMapper);
    }

    @Test
    void contextLoads()
    {
        String token = JWTUtils.sign(123L);
        System.out.println(token);

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long userId = JWTUtils.getUserId(token);
        boolean verify = JWTUtils.verify(token);
        System.out.println("userId = " + userId);
        System.out.println("verify = " + verify);
        Boolean expired = JWTUtils.isExpired(token);
        System.out.println("expired = " + expired);
    }

    @Test
    void test()
    {
        String string = new String("123456");
        string = string.replace("a","");
        System.out.println(string);
    }

    @Test
    public void BookTest() throws ParseException
    {
        BookDto bookDto = new BookDto();
        bookDto.setDescription("描述");
        bookDto.setName("RocketMQ技术内幕");
        ArrayList<String> tagList = new ArrayList<>();
        tagList.add("1");
        tagList.add("2");
        bookDto.setTagIdList(tagList);
        ArrayList<String> photoList = new ArrayList<>();
        photoList.add("path3");
        photoList.add("path4");
        bookDto.setPhotoList(photoList);
        bookDto.setUserId(1);
        bookDto.setType(1);

        bookService.uploadBookInfo(bookDto);
    }

    @Test
    void UserTest()
    {
        Result bookInfo = userService.getBookInfo(1, 1);
        System.out.println(bookInfo);
    }

    @Test
    void getBookList()
    {
        Result bookInfo = bookService.getBookInfo(1);
        System.out.println(bookInfo);
    }

    @Test
    void getTagsList()
    {
        Result allTags = tagService.getAllTags(1);
        System.out.println(allTags);
    }

    @Test
    void addTagTest()
    {
        Result result = tagService.addTag(1, "Python");
        System.out.println(result);
    }

    @Test
    void ftpTest()
    {
        try {
            FTPUtils.delete("/2022-01-22/228c3410-cb0b-49be-8944-06ec049416ae-ByteDance.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private FavoriteService favoriteService;

    @Test
    void StringTest()
    {
        Result favorites = favoriteService.getFavorites(1);
        System.out.println(favorites);
    }

    @Test
    void deleteFavTest()
    {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Result result = favoriteService.deleteFavorite(1, list);
        System.out.println(result);
    }

    @Test
    void mobileTest()
    {
        Pattern pattern = Pattern.compile("^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$");
        Matcher matcher = pattern.matcher("15089123434");
        if (!matcher.find())
        {
            System.out.println("不合法");
        }else
        {
            System.out.println("合法");
        }
    }

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void esTest()
    {

            BookDto bookDto = new BookDto();
            bookDto.setDescription("99成新");
            bookDto.setName("Rocket技术内幕");
            ArrayList<String> tagList = new ArrayList<>();
            tagList.add("1");
            tagList.add("2");
            bookDto.setTagIdList(tagList);
            ArrayList<String> photoList = new ArrayList<>();
            photoList.add("images1");
            photoList.add("images2");
            bookDto.setPhotoList(photoList);
            bookDto.setUserId(1);
            bookDto.setId(22);
            bookDto.setType(1);

            userService.updateBookInfo(bookDto);
    }

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Test
    public void searchTest()
    {
        RequestParams requestParams = new RequestParams();
        requestParams.setType("book");
        requestParams.setContent("英语读写三");
        requestParams.setMinPrice(20);
        requestParams.setMaxPrice(40);
        Result search = elasticsearchService.search(requestParams);
        System.out.println(search);
    }

    @Test
    public void updateBook()
    {
        userService.withdrawBookInfo(20,1);
    }

    @Test
    public void deleteBook()
    {
        userService.deleteHistory(20,1);
    }

    @Autowired
    private WechatServiceImpl wechatService;

    @Test
    public void registryTest()
    {
        User user = new User();
        user.setNickname("秃头懒狗");
        user.setUsername("小明");
        user.setPassword("123456");
        user.setGender("男");
        user.setAvatar("Sabrina's avatar");
        user.setMobile("15848134876");
        wechatService.registry(user);
    }

    @Test
    public void searchUserTest()
    {
        RequestParams requestParams = new RequestParams();
        requestParams.setType(ElasticsearchIndex.USER);
        requestParams.setContent("Tayl");
        Result search = elasticsearchService.search(requestParams);
        System.out.println(search);
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void redisTest()
    {

    }

    @Test
    public void redisSetTest()
    {
        User user = new User();
        user.setId(1L);
        user.setNickname("admin");
        wechatService.login(user);
    }

    @Test
    public void redisGetTest()
    {
        UserDto userDto = (UserDto) redisTemplate.opsForValue()
                .get("token_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNjQ0MDU0MTkxfQ.UbmPkixMS_fUXdkzRm6hZ03qNdOfp9GkdqgBAHBNViw");
        System.out.println(userDto);
    }

}
