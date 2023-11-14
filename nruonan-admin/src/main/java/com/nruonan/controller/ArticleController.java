package com.nruonan.controller;


import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.ArticleDto;
import com.nruonan.domain.dto.AddArticleDto;
import com.nruonan.domain.entity.Article;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }

    @GetMapping("/list")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Article article){
        PageVo pageVo = articleService.selectArticlePage(pageNum,pageSize,article);
        return ResponseResult.okResult(pageVo);
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable("id") Long id){
        return articleService.getInfo(id);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
