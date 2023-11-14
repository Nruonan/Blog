package com.nruonan.utils;


import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.Menu;
import com.nruonan.domain.vo.MenuTreeVo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 35238
 * @date 2023/8/10 0010 15:17
 */
//新增角色-获取菜单下拉树列表
public class SystemConverter {

    private SystemConverter() {}

    public static List<MenuTreeVo> buildMenuSelectTree(List<Menu> menus) {
        List<MenuTreeVo> menuTreeVos = menus.stream()
                .map(menu -> new MenuTreeVo(menu.getId(), menu.getMenuName(), menu.getParentId(), null))
                .collect(Collectors.toList());
        List<MenuTreeVo> options = menuTreeVos.stream()
                .filter(menuTreeVo -> menuTreeVo.getParentId().equals(0L))
                .map(menuTreeVo -> menuTreeVo.setChildren(getChildList(menuTreeVos, menuTreeVo)))
                .collect(Collectors.toList());
        return options;
    }


    /**
     * 得到子节点列表
     */
    private static List<MenuTreeVo> getChildList(List<MenuTreeVo> list, MenuTreeVo option) {
        List<MenuTreeVo> collect = list.stream()
                .filter(o -> Objects.equals(o.getParentId(), option.getId()))
                .map(o -> o.setChildren(getChildList(list,o)))
                .collect(Collectors.toList());
        return collect;

    }
}