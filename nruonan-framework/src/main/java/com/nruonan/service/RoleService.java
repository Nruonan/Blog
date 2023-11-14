package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.RoleDto;
import com.nruonan.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-11-10 15:21:57
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult selectRolePage(Integer pageNum, Integer pageSize, Role role);

    ResponseResult insertRole(Role role);

    ResponseResult updateRole(Role role);


    List<Role> selectRoleAll();


    List<Long> selectRoleIdByUserId(Long id);
}
