package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.Result;
import com.turing.entity.Book;
import com.turing.entity.dto.BookDto;
import com.turing.mapper.BookMapper;
import com.turing.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 16:05:43
 */
@Service
public class BookServiceImpl implements BookService
{

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Result uploadBookInfo(BookDto bookDto) throws ParseException
    {
        Book book = new Book();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        book.transform(bookDto);
        book.setCreatedTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
        bookMapper.insert(book);
        bookDto.setId(book.getId());
        return new Result().success(bookDto);
    }

    @Override
    public Result getBookInfo(Integer type)
    {
        List<Book> books = bookMapper.selectList(new QueryWrapper<Book>().eq("type", type).eq("status",1));
        for (Book book : books) {
            System.out.println(book);
        }
        ArrayList<BookDto> bookDtoList = new ArrayList<>();
        for (Book book : books) {
            BookDto bookDto = new BookDto();
            bookDto.transform(book);
            System.out.println(bookDto);
            bookDtoList.add(bookDto);
        }
        return new Result().success(bookDtoList);
    }

    @Override
    public Result getBookInfoByBookId(Integer bookId)
    {
        Book book = bookMapper.selectById(bookId);
        return new Result().success(book);
    }
}
