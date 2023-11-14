package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.ChangeRoleStatusDto;
import com.nruonan.domain.dto.RoleDto;
import com.nruonan.domain.entity.Menu;
import com.nruonan.domain.entity.Role;
import com.nruonan.domain.vo.MenuTreeVo;
import com.nruonan.service.MenuService;
import com.nruonan.service.RoleService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.SystemConverter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {
     @Resource
     private RoleService roleService;
     @Resource
     private MenuService menuService;

     @GetMapping("/list")
     public ResponseResult selectRolePage(Integer pageNum, Integer pageSize, Role role){
         return roleService.selectRolePage(pageNum, pageSize,role);
     }
     @PutMapping("/changeStatus")
     public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleStatusDto){
          Role role = new Role();
          role.setId(roleStatusDto.getRoleId());
          role.setStatus(roleStatusDto.getStatus());
          return ResponseResult.okResult(roleService.updateById(role));
     }

     @PostMapping
     public ResponseResult addRole(@RequestBody Role role){
          return roleService.insertRole(role);
     }

     @GetMapping(value = "/{roleId}")
     public ResponseResult getInfo(@PathVariable Long roleId) {
          Role role = roleService.getById(roleId);
          return ResponseResult.okResult(role);
     }
     @PutMapping
     public ResponseResult update(@RequestBody Role role){
          return roleService.updateRole(role);
     }
     @DeleteMapping("/{roleId}")
     public ResponseResult deleteRole(@PathVariable("roleId") Long roleId){
          roleService.removeById(roleId);
          return ResponseResult.okResult();
     }

     @GetMapping("/listAllRole")
     //①查询角色列表接口
     public ResponseResult listAllRole(){
          List<Role> roles = roleService.selectRoleAll();
          return ResponseResult.okResult(roles);
     }
}
