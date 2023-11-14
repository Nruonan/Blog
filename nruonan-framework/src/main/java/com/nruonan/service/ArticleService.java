package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.ArticleDto;
import com.nruonan.domain.dto.AddArticleDto;
import com.nruonan.domain.entity.Article;
import com.nruonan.domain.vo.PageVo;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2023-11-06 14:27:24
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    PageVo selectArticlePage(Integer pageNum, Integer pageSize, Article article);

    ResponseResult getInfo(Long id);

    ResponseResult updateArticle(ArticleDto articleDto);

}
