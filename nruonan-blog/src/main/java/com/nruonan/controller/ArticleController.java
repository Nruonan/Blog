package com.nruonan.controller;




import com.nruonan.ResponseResult;
import com.nruonan.annotation.SystemLog;
import com.nruonan.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文章表(Article)表控制层
 *
 * @author makejava
 * @since 2023-11-06 14:33:28
 */
@RestController
@RequestMapping("/article")
public class ArticleController  {

    @Resource
    private ArticleService articleService;

    /*@GetMapping("/list")
    public List<Article> test(){
        return articleService.list();
    }*/
    @SystemLog(businessName = "获取热门文章")
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        // 查询热门文章， 封装乘ResponseResult
        return articleService.hotArticleList();
    }
    @SystemLog(businessName = "查询分类文章信息")
    @GetMapping("/articleList")
    public ResponseResult articleList( Integer pageNum, Integer pageSize, Long categoryId){
        // 查询分类文章
        return articleService.articleList(pageNum, pageSize, categoryId);
    }
    @SystemLog(businessName = "查看文章详细信息")
    @GetMapping("/{id}")
    public ResponseResult article(@PathVariable("id") Long id){
        // 查询文章详细信息
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}") // 更新浏览量
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}

