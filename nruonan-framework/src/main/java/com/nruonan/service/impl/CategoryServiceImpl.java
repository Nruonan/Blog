package com.nruonan.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.ResponseResult;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.entity.Article;
import com.nruonan.domain.entity.Category;
import com.nruonan.domain.vo.CategoryVo;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.mapper.CategoryMapper;
import com.nruonan.service.ArticleService;
import com.nruonan.service.CategoryService;
import com.nruonan.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-11-06 22:00:04
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private ArticleService articleService;
    @Override
    //查询分类列表的核心代码
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 要求查的是status为0
        wrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 查询文章列表
        List<Article> articleList = articleService.list(wrapper);
        // 获取文章的分类id，去重 使用stream流获取
        Set<Long> categoryIds = articleList.stream()
                .map(new Function<Article, Long>() {
                    @Override
                    public Long apply(Article article) {
                        return article.getCategoryId();
                    }
                })
                .collect(Collectors.toSet());
        // 查询分流类
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> category.getStatus().equals(SystemConstants.STATUS_NORMAL))
                .collect(Collectors.toList());
        // 封装乘CategoryVo实体类返回给前端,CategoryVo只返回前端需要的字段
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult categoryList(Integer pageNum, Integer pageSize, Category category) {
        // 状态 名字模糊查询
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(category.getStatus()),Category::getStatus,category.getStatus())
                .like(StringUtils.hasText(category.getName()),Category::getName,category.getName());
        // 分页
        Page<Category> page = new Page(pageNum,pageSize);
        page(page,wrapper);
        List<Category> categories = page.getRecords();
        // 封装数据
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        PageVo pageVo = new PageVo(categoryVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}
