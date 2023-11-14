package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.AddTagDto;
import com.nruonan.domain.dto.TagListDto;
import com.nruonan.domain.entity.Tag;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.domain.vo.TagVo;

import java.util.List;

/**
 * @author 35238
 * @date 2023/8/2 0002 21:14
 */

public interface TagService extends IService<Tag> {


    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);


    List<TagVo> listAllTag();
}