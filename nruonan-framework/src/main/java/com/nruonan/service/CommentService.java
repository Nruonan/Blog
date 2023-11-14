package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.Comment;
import com.nruonan.handler.exception.SystemException;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-11-08 09:58:51
 */
public interface CommentService extends IService<Comment> {
    //查询评论区的评论
    ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);


    ResponseResult addComment(Comment comment) throws SystemException;
}
