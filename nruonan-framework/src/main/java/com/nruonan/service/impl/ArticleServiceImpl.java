package com.nruonan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.ResponseResult;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.dto.ArticleDto;
import com.nruonan.domain.dto.AddArticleDto;
import com.nruonan.domain.entity.Article;
import com.nruonan.domain.entity.ArticleTag;
import com.nruonan.domain.entity.Category;
import com.nruonan.domain.vo.*;
import com.nruonan.mapper.ArticleMapper;
import com.nruonan.service.ArticleService;

import com.nruonan.service.ArticleTagService;
import com.nruonan.service.CategoryService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2023-11-06 14:27:25
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    //注入我们写的CategoryService接口
    private CategoryService categoryService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleTagService articleTagService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回。把所有查询条件写在queryWrapper里面
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 排名不能有草稿的 status必须为0, 以阅读量排序
        wrapper
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getViewCount);
        // 构造分页，1 10
        Page<Article> page = new Page<>(SystemConstants.ARTICLE_STATUS_DRAFT, SystemConstants.ARTICLE_STATUS_SIZE);
        page(page,wrapper);
        List<Article> articles = page.getRecords();

        articles = articles.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        // 利用redis更新viewCount
                        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, article.getId().toString());
                        article.setViewCount(Long.valueOf(viewCount));
                        return article;
                    }
                }).collect(Collectors.toList());
        // bean拷贝
        List<HotArticleVo> articleVos = new ArrayList<>();
       /* for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article,vo);
            articleVos.add(vo);
        }*/
        articleVos  = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 如果有categoryId就要和传入的相同
        // 状态status 为0，
        // 置顶为1时放在头部
        wrapper
                .eq(Objects.nonNull(categoryId) && categoryId > 0,Article::getCategoryId,categoryId)
                .eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getIsTop);

        // 进行分页查询
        Page<Article> page ;
        if(pageNum == null || pageSize == null || pageNum == 0 || pageSize ==0){
            page = new Page<>(1 , 10);
        } else {
            page = new Page<>(pageNum, pageSize);
        }
        page(page,wrapper);

        /**
         * 解决categoryName字段没有返回值的问题。在分页之后，封装成ArticleListVo之前，进行处理
         */
        //用categoryId来查询categoryName(category表的name字段)，也就是查询'分类名称'。有两种方式来实现，如下
        List<Article> articles = page.getRecords();

        //第一种方式，用for循环遍历的方式
       /* for (Article article : articles) {
            //'article.getCategoryId()'表示从article表获取category_id字段，然后作为查询category表的name字段
            Category category = categoryService.getById(article.getCategoryId());
            //把查询出来的category表的name字段值，也就是article，设置给Article实体类的categoryName成员变量
            article.setCategoryName(category.getName());

        }*/

        //第二种方式，用stream流的方式
        articles = articles.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        // 获取分类id 查询分类信息，获取分类名称
                        Category category = categoryService.getById(article.getCategoryId());
                        String name = category.getName();
                        // 把分类名称设置给article
                        article.setCategoryName(name);

                        // 利用redis更新viewCount
                        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, article.getId().toString());
                        article.setViewCount(Long.valueOf(viewCount));
                        return article;
                    }
                })
                .collect(Collectors.toList());

        // 封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        return ResponseResult.okResult(new PageVo(articleListVos,page.getTotal()));
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 查询当前文章
        Article article = getById(id);
        // 从redis获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        article.setViewCount(Long.valueOf(viewCount));
        // 把最后的查询结果封装成ArticleListVo(我们写的实体类)。BeanCopyUtils是我们写的工具类
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        // 设置分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category != null)
            articleDetailVo.setCategoryName(category.getName());

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        // 更新redis中对应 的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT ,id.toString(),1);
        return null;
    }
    // 添加博文
    @Override
    @Transactional //出现问题 回滚
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> tags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        // 添加 博客和标签的关联
        articleTagService.saveBatch(tags);
        return ResponseResult.okResult();
    }
    // 后台查询
    @Override
    public PageVo selectArticlePage(Integer pageNum, Integer pageSize, Article article) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 寻找title summary相关的
        wrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle,article.getTitle())
                .like(StringUtils.hasText(article.getSummary()),Article::getSummary,article.getSummary());
        Page<Article> page = new Page(pageNum,pageSize);
        page(page,wrapper);
        // 得到页码的链表
        List<Article> articles = page.getRecords();
        PageVo pageVo = new PageVo(articles,page.getTotal());

        return pageVo;
    }

    @Override
    public ResponseResult getInfo(Long id) {
        // 获取文章
        Article article = getById(id);
        // 通过articleTag 获取关联标签
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> articleTags = articleTagService.list(wrapper);

        List<Long> tags = articleTags.stream().
                map(ArticleTag::getTagId)
                .collect(Collectors.toList());
        // 封装Article
        ArticleByIdVo articleByIdVo = BeanCopyUtils.copyBean(article, ArticleByIdVo.class);
        articleByIdVo.setTags(tags);
        return ResponseResult.okResult(articleByIdVo);
    }

    @Override
    public ResponseResult updateArticle(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto,Article.class);
        // 更新博客
        updateById(article);
        // 删掉旧的标签博客 关联
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(wrapper);
        // 添加新的标签
        List<ArticleTag> tags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(tags);
        return ResponseResult.okResult();
    }

}
