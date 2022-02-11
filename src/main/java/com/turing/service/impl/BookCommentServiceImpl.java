package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.turing.common.CommentStatus;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.Book;
import com.turing.entity.BookComment;
import com.turing.entity.User;
import com.turing.entity.dto.BookCommentDto;
import com.turing.entity.dto.BookDto;
import com.turing.mapper.BookCommentMapper;
import com.turing.mapper.BookMapper;
import com.turing.mapper.UserMapper;
import com.turing.service.BookCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 17:07:49
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class BookCommentServiceImpl implements BookCommentService
{
    @Autowired
    private BookCommentMapper commentMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result comment(BookCommentDto bookCommentDto)
    {
        Book book = bookMapper.selectById(bookCommentDto.getBookId());
        if (book == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("该书籍资料不存在,评论失败!");
        }
        User user = userMapper.selectById(bookCommentDto.getUserId());
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("该用户不存在,评论失败!");
        }
        if (bookCommentDto.getContent() == null || bookCommentDto.getType() == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("评论内容或评论类型不能为空!");
        }
        BookComment bookComment = new BookComment();
        bookComment.transform(bookCommentDto);

        //修改图书评论数量以及好评率
        //更新评论数量
        book.setCommentCount(book.getCommentCount() + 1);
        Double rate = book.getActiveRate();
        Integer commentCount = book.getCommentCount();
        //得到好评数量
        Integer result = commentMapper.selectCount(new QueryWrapper<BookComment>()
                .eq("book_id", bookCommentDto.getBookId())
                .eq("type", CommentStatus.POSITIVE.getCode()));
        //好评
        if (bookComment.getType()!= null && bookComment.getType().equals(CommentStatus.POSITIVE.getCode()))
        {
            //好评加一
            result++;
        }
        //差/中评 直接原有的好评数量/评论数量
        rate = Double.valueOf(result) / Double.valueOf(commentCount);
        rate = rate * 100;
        DecimalFormat decimalFormat = new DecimalFormat("######00.00");
        rate = Double.valueOf(decimalFormat.format(rate));
        //更新好评率
        book.setActiveRate(rate);
        //新增评论
        commentMapper.insert(bookComment);
        bookMapper.updateById(book);
        BookDto bookDto = new BookDto();
        bookDto.transform(book);
        bookCommentDto.setId(bookComment.getId());
        redisTemplate.opsForHash().put(RedisKey.BOOK_KEY+bookCommentDto.getBookId(),
                RedisKey.BOOK_COMMENT_FILED+bookComment.getId(),
                bookCommentDto);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("book",bookDto);
        resultMap.put("comment",bookComment);
        return new Result().success(resultMap);
    }

    @Override
    public Result getCommentByPage(Integer page, Integer size, Integer bookId)
    {
        //数据库查询
        Page<BookComment> commentPage = new Page<>(page, size);
        Page<BookComment> pageInfo = commentMapper.selectPage(commentPage, new QueryWrapper<BookComment>().eq("book_id", bookId));
        List<BookComment> bookCommentList = pageInfo.getRecords();
        if (bookCommentList == null || bookCommentList.isEmpty())
        {
            return new Result().success("暂无评论信息!");
        }
        List<BookCommentDto> bookCommentDtoList = new ArrayList<>();
        for (BookComment bookComment : bookCommentList) {
            BookCommentDto bookCommentDto = new BookCommentDto();
            bookCommentDto.transform(bookComment);
            bookCommentDtoList.add(bookCommentDto);
        }
        return new Result().success(bookCommentDtoList);
    }


/*
            List<String> commentTagList = bookCommentDto.getCommentTag();
        String bookCommentTag = book.getCommentTag();
        if (commentTagList!=null && !commentTagList.isEmpty())
        {
            if (bookCommentTag == null)
            {
                for (int i = 0; i < commentTagList.size(); i++) {
                    StringBuilder tagString = new StringBuilder();
                    if (i < commentTagList.size() - 1)
                    {
                        tagString.append(commentTagList.get(i)+":1,");
                    }else
                    {
                        tagString.append(commentTagList.get(i)+":1");
                    }
                }
            }else
            {
                //
                String[] tags = bookCommentTag.split(",");
                for (String tag : tags) {
                    String[] split = tag.split(":");

                    for (String commentTag : commentTagList) {
                        if (commentTag.equals(split[0]))
                        {
                            Integer num = Integer.parseInt(split[1]);
                            split[1] = num.toString();
                        }
                    }
                }
            }
        }

 */
}
