package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-11-07 15:20:05
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult linkList(Integer pageNum, Integer pageSize, Link link);
}
