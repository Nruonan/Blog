package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.annotation.SystemLog;
import com.nruonan.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @SystemLog(businessName = "获取分类信息")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }
}
