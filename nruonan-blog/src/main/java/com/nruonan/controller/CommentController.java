package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.dto.AddCommentDto;
import com.nruonan.domain.entity.Comment;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.service.CommentService;
import com.nruonan.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关")
public class CommentController {
    @Resource
    //CommentService是我们在nruonan-framework工程写的类
    private CommentService commentService;

    @GetMapping("commentList")
    //ResponseResult是我们在nruonan-framework工程写的类
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    @PostMapping
    //在文章的评论区发送评论。ResponseResult是我们在huanf-framework工程写的类
    public ResponseResult addComment(@RequestBody Comment comment) throws SystemException {
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    //在友链的评论区发送评论。ResponseResult是我们在huanf-framework工程写的类
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        //commentService是我们刚刚实现文章的评论区发送评论功能时(当时用的是addComment方法，现在用commentList方法)，写的类
        //SystemCanstants是我们写的用来解决字面值的常量类，Article_LINK代表1
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
