package com.nruonan.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.AddCategoryDto;
import com.nruonan.domain.entity.Category;
import com.nruonan.domain.vo.CategoryVo;
import com.nruonan.domain.vo.ExcelCategoryVo;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.service.CategoryService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("content/category")
public class CategoryController {
   @Resource
   private CategoryService categoryService;

   @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
       List<CategoryVo> categoryVos = categoryService.listAllCategory();
       return ResponseResult.okResult(categoryVos);
   }
   @PreAuthorize("@ps.hasPermission('content:category:export')")
   @GetMapping("/export")
   public void export(HttpServletResponse response){
      // 设置下载文件的请求头
      try {
         WebUtils.setDownLoadHeader("分类.xlsx",response);
         // 获取需要到出的数据
         List<Category> categorys = categoryService.list();
         List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categorys, ExcelCategoryVo.class);
         // 把数据写入到excel
         EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                 .doWrite(excelCategoryVos);
      } catch (Exception e) {
         e.printStackTrace();
         // 如果出现异常也要响应json
         ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
         WebUtils.renderString(response, JSON.toJSONString(result));

      }
   }

   @GetMapping("/list")
   public ResponseResult categoryList(Integer pageNum,Integer pageSize,Category category){
      return categoryService.categoryList(pageNum,pageSize,category);
   }

   @PostMapping
   public ResponseResult add(@RequestBody AddCategoryDto addCategoryDto){
      Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
      categoryService.save(category);
      return ResponseResult.okResult();
   }
   @GetMapping("/{id}")
   public ResponseResult getInfo(@PathVariable("id") Long id){
      Category category = categoryService.getById(id);
      return ResponseResult.okResult(category);
   }
   @PutMapping
   public ResponseResult update(@RequestBody CategoryVo categoryVo){
      Category category = BeanCopyUtils.copyBean(categoryVo, Category.class);
      categoryService.updateById(category);
      return ResponseResult.okResult();
   }
   @DeleteMapping("/{id}")
   public ResponseResult delete(@PathVariable("id")Long id){
      categoryService.removeById(id);
      return ResponseResult.okResult();
   }
}
