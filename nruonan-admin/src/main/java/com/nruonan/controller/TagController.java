package com.nruonan.controller;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.AddTagDto;
import com.nruonan.domain.dto.EditTagDto;
import com.nruonan.domain.dto.TagListDto;
import com.nruonan.domain.entity.Tag;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.domain.vo.TagVo;
import com.nruonan.service.TagService;
import com.nruonan.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 35238
 * @date 2023/8/2 0002 21:27
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    //TagService是我们在nruonan-framework工程的接口
    private TagService tagService;

    //查询标签列表
    @GetMapping("/list")
    //ResponseResult是我们在nruonan-framework工程的实体类
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto ){
        //list是mybatisplus提供的方法
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    // 新增标签
    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        Tag tag = BeanCopyUtils.copyBean(addTagDto,Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }
    // 删除标签
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }
    // 获取标签信息
    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable("id") Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }
    // 修改标签
    @PutMapping()
    public ResponseResult updateTag(@RequestBody EditTagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> tagVos = tagService.listAllTag();
        return ResponseResult.okResult(tagVos);
    }
}