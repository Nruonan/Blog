package com.nruonan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.ResponseResult;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.entity.Link;
import com.nruonan.domain.vo.LinkVo;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.mapper.LinkMapper;
import com.nruonan.service.LinkService;
import com.nruonan.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-11-07 15:20:06
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的友链
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        //要求 查的是友链表status字段的值为0
        wrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(wrapper);
        // 转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult linkList(Integer pageNum, Integer pageSize, Link link) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(link.getStatus()),Link::getStatus,link.getStatus())
                .like(StringUtils.hasText(link.getName()),Link::getName,link.getName());
        Page<Link> page = new Page(pageNum,pageSize);
        page(page,wrapper);
        List<Link> links = page.getRecords();
        PageVo pageVo = new PageVo(links,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}
