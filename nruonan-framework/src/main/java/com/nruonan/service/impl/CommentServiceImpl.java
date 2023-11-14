package com.nruonan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.ResponseResult;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.entity.Article;
import com.nruonan.domain.entity.Comment;
import com.nruonan.domain.vo.CommentVo;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.handler.mybatisplus.MyMetaObjectHandler;
import com.nruonan.mapper.CommentMapper;
import com.nruonan.service.CommentService;
import com.nruonan.service.UserService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-11-08 09:58:52
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService{

    @Autowired
    //根据userid查询用户信息，也就是查username
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        //对articleId进行判断，作用是得到指定的文章
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);

        //对评论区的某条评论的rootID进行判断，如果为-1，就表示是根评论。SystemCanstants是我们写的解决字面值的类
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT);
        //文章的评论，避免查到友链的评论
        queryWrapper.eq(Comment::getType,commentType);
        //分页查询。查的是整个评论区的每一条评论
        Page<Comment> page  = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //调用下面那个方法。根评论排序
        List<Comment> sortedComments = page.getRecords().stream()
                .sorted(Comparator.comparing(Comment::getCreateTime))
                .collect(Collectors.toList());
        List<CommentVo> commentVoList = xxToCommentList(sortedComments);

        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //把查到的children子评论集的集合，赋值给commentVo类的children字段
            commentVo.setChildren(children);

        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }
    //根据根评论的id，来查询对应的所有子评论(注意子评论只查到二级评论，不再往深查)
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        //对子评论按照时间进行排序
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        //调用下面那个方法
        List<CommentVo> commentVos = xxToCommentList(comments);
        return commentVos;
    }

    //封装响应返回。CommentVo、BeanCopyUtils、ResponseResult、PageVo是我们写的类
    private List<CommentVo> xxToCommentList(List<Comment> list){
        //获取评论区的所有评论
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历。由于封装响应好的数据里面没有username字段，所以我们还不能返回给前端。这个遍历就是用来得到username字段
        for (CommentVo commentVo : commentVos) {
            //
            if(userService.getById(commentVo.getCreateBy()) == null){
                break;
            }
            //需要根据commentVo类里面的createBy字段，然后用createBy字段去查询user表的nickname字段(子评论的用户昵称)
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            //然后把nickname字段(发这条子评论的用户昵称)的数据赋值给commentVo类的username字段
            commentVo.setUsername(nickName);

            //查询根评论的用户昵称。怎么判断是根评论的用户呢，判断getToCommentUserId为1，就表示这条评论是根评论
            if(commentVo.getToCommentUserId() != -1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                //然后把nickname字段(发这条根评论的用户昵称)的数据赋值给commentVo类的toCommentUserName字段
                commentVo.setToCommentUserName(toCommentUserName);
            }

        }

        //返回给前端
        return commentVos;
    }

    //在文章的评论区发送评论
    @Override
    public ResponseResult addComment(Comment comment) throws SystemException {
        //注意前端在调用这个发送评论接口时，在请求体是没有向我们传入createTime、createId、updateTime、updateID字段，所以
        //我们这里往后端插入数据时，就会导致上面那行的四个字段没有值
        //为了解决这个问题，我们在huanf-framework工程新增了MyMetaObjectHandler类、修改了Comment类。详细可自己定位去看一下代码

        //限制用户在发送评论时，评论内容不能为空。如果为空就抛出异常
        if(!StringUtils.hasText(comment.getContent())){
            //AppHttpCodeEnum是我们写的枚举类，CONTENT_NOT_NULL代表提示''
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }

        //解决了四个字段没有值的情况，就可以直接调用mybatisplus提供的save方法往数据库插入数据(用户发送的评论的各个字段)了
        save(comment);

        //封装响应返回
        return ResponseResult.okResult();
    }
}