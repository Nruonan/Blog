package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.domain.entity.RoleMenu;


/**
 * @author 35238
 * @date 2023/8/10 0010 15:47
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);
}