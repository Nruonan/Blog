package com.nruonan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.ResponseResult;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.dto.RoleDto;
import com.nruonan.domain.entity.Role;
import com.nruonan.domain.entity.RoleMenu;
import com.nruonan.domain.entity.UserRole;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.mapper.RoleMapper;
import com.nruonan.service.RoleMenuService;
import com.nruonan.service.RoleService;
import com.nruonan.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-11-10 15:21:57
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleService roleService;
    
    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        // 判断是否是管理员， 返回集合中只需要admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        // 否则查询用户所具有的角色信息

        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult selectRolePage(Integer pageNum, Integer pageSize, Role role) {
        // 搜索 状态 ，模糊名字，排序
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(role.getStatus()),Role::getStatus,role.getStatus())
                .like(StringUtils.hasText(role.getRoleName()),Role::getRoleName,role.getRoleName())
                .orderByAsc(Role::getRoleSort);
        // 分页查询
        Page<Role> page = new Page(pageNum,pageSize);
        page(page,wrapper);
        // 封装数据到RoleDto
        List<Role> roles = page.getRecords();
        List<RoleDto> roleDtos = BeanCopyUtils.copyBeanList(roles, RoleDto.class);
        // 返回数据
        PageVo pageVo = new PageVo(roleDtos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult insertRole(Role role) {
        save(role);
        return ResponseResult.okResult();
    }

    //-----------------------修改角色-保存修改好的角色信息----------------------------
    @Override
    public ResponseResult updateRole(Role role) {
        // 更新角色信息
        roleService.updateById(role);
        // 删除旧的角色菜单关联
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        // 添加新的关联
        insertRoleMenu(role);  // 添加rolemenu关联
        return null;
    }

    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }

    @Override
    public List<Role> selectRoleAll() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = list(wrapper);
        return list;
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long id) {
        return getBaseMapper().selectRoleIdByUserId(id);

    }
}
