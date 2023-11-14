package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.Category;
import com.nruonan.domain.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-11-06 22:00:04
 */
public interface CategoryService extends IService<Category> {
    //查询文章分类的接口
    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult categoryList(Integer pageNum, Integer pageSize, Category category);
}
