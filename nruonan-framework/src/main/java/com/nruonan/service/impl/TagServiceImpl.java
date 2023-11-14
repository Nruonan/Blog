package com.nruonan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.AddTagDto;
import com.nruonan.domain.dto.TagListDto;
import com.nruonan.domain.entity.Tag;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.domain.vo.TagVo;
import com.nruonan.mapper.TagMapper;
import com.nruonan.service.TagService;
import com.nruonan.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 35238
 * @date 2023/8/2 0002 21:15
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagService tagService;
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // 分页查询
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName, tagListDto.getName())
                .eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setPages(pageSize);
        page(page, wrapper);
        List<Tag> records = page.getRecords();
        PageVo pageVo = new PageVo(records,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public List<TagVo> listAllTag() {
        List<Tag> list = list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);

        return tagVos;
    }
}