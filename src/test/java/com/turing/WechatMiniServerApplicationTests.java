package com.turing;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.dto.BookDto;
import com.turing.mapper.BookMapper;
import com.turing.service.BookService;
import com.turing.service.FavoriteService;
import com.turing.service.TagService;
import com.turing.service.UserService;
import com.turing.service.impl.BookServiceImpl;
import com.turing.utils.FTPUtils;
import com.turing.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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


}
