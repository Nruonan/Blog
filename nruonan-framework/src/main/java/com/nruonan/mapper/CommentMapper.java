package com.nruonan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nruonan.domain.entity.Comment;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Nruonan
 * @description
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
