package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-11-10 15:15:55
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> selectMenuList(Menu menu);
    //删除菜单-判断是否存在子菜单
    boolean hasChild(Long menuId);

    List<Long> selectMenuListByRoleId(Long id);
}
