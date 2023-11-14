package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.EditMenuDto;
import com.nruonan.domain.entity.Menu;
import com.nruonan.domain.vo.MenuTreeVo;
import com.nruonan.domain.vo.MenuVo;
import com.nruonan.domain.vo.RoleMenuTreeSelectVo;
import com.nruonan.service.MenuService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.SystemConverter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(Menu menu){
        List<Menu> menus      = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        EditMenuDto editMenuDto = BeanCopyUtils.copyBean(menu, EditMenuDto.class);
        return ResponseResult.okResult(editMenuDto);
    }

    @PutMapping
    //②然后才是更新菜单
    public ResponseResult edit(@RequestBody Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult delete(@PathVariable("menuId") Long id){

        if(menuService.hasChild(id)){
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
        menuService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable("id") Long id){
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(id);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }
}
