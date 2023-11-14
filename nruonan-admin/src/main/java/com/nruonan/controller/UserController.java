package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.UserDto;
import com.nruonan.domain.entity.Role;
import com.nruonan.domain.entity.User;
import com.nruonan.domain.vo.UserInfoAndRoleIdsVo;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.service.RoleService;
import com.nruonan.service.UserService;
import com.nruonan.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize){
        return userService.Userslist(user,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult add(@RequestBody User user) throws SystemException {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!userService.checkPhoneUnique(user)){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!userService.checkEmailUnique(user)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(user);
    }
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable("id") List<Long> ids){
        if(ids.contains(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
        }
        return userService.delete(ids);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserInfoAndRoleIds(@PathVariable("id") Long id){
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(id);
        List<Long> roleIds = roleService.selectRoleIdByUserId(id);

        UserInfoAndRoleIdsVo userInfoAndRoleIdsVo = new UserInfoAndRoleIdsVo(user, roles, roleIds);
        return ResponseResult.okResult(userInfoAndRoleIdsVo);
    }
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        return userService.updateUser(user);
    }

}
