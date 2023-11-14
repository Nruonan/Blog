package com.nruonan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.entity.Menu;
import com.nruonan.domain.vo.MenuVo;
import com.nruonan.mapper.MenuMapper;
import com.nruonan.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-11-10 15:15:58
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public List<String> selectPermsByUserId(Long id) {

        // 根据用户Id查询权限关键字
        // 如果是管理返回所有权限
        if(id == 1L){
            // 返回所有权限
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL)
                    .in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            List<Menu> menus = list(wrapper);
            List<String> collect = menus.stream()
                    .map(menu -> menu.getPerms())
                    .collect(Collectors.toList());
            return collect;
        }
        return getBaseMapper().selectPermByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否管理员
        if(userId.equals(1L)){
            //如果是 返回所有符合要求的menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            // 返回当前用户所具有的menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        // 构建tree
        // 先找出第一层的菜单，再找子菜单
        List<Menu> menuTree = builderMenuTree(menus,0L);

        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus,Long parentId) {
        List<Menu> list = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu,menus)))
                .collect(Collectors.toList());
        return list;
    }
    // 获取传入参数的子menu
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> list = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m ->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return list;
    }
    // 实现后端模糊查询 状态
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        // 菜单名模糊查询，status = 0, 还要按照父菜单id和orderNum排序
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,SystemConstants.STATUS_NORMAL)
                .like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName())
                .orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> list = list(wrapper);
        return list;
    }

    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        return count(queryWrapper) != 0;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long id) {
        return getBaseMapper().selectMenuListByRoleId(id);
    }
}
