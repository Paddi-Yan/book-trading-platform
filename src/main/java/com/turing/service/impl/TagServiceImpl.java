package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Tag;
import com.turing.entity.User;
import com.turing.entity.dto.TagDto;
import com.turing.mapper.TagMapper;
import com.turing.mapper.UserMapper;
import com.turing.service.TagService;
import com.turing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 22:13:26
 */
@Service
public class TagServiceImpl implements TagService
{
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result getAllTags(Integer userId)
    {
        List<Tag> publicTags = tagMapper.selectList(new QueryWrapper<Tag>().isNull("user_id"));
        List<Tag> privateTags = tagMapper.selectList(new QueryWrapper<Tag>().isNotNull("user_id"));
        TagDto tagDto = new TagDto(publicTags, privateTags);
        return new Result().success(tagDto);
    }

    @Override
    public Result addTag(Integer userId, String tagName)
    {
        Tag tag = new Tag(tagName,userId);
        tagMapper.insert(tag);
        return new Result().success(tag);
    }

    @Override
    public Result deleteTag(Integer userId, Integer tagId)
    {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<Tag>().eq("user_id", userId).eq("id", tagId);
        Tag tag = tagMapper.selectOne(queryWrapper);
        if (tag == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户无删除该标签权限!");
        }
        tagMapper.delete(queryWrapper);
        return new Result().success(tag);
    }

    @Override
    public Result editTag(Tag tag)
    {
         tagMapper.update(tag,new QueryWrapper<Tag>().eq("id",tag.getId()).eq("user_id",tag.getUserId()));
         return new Result().success(tag);
    }
}
