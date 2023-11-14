package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.LoginUser;
import com.nruonan.domain.entity.Menu;
import com.nruonan.domain.entity.User;
import com.nruonan.domain.vo.AdminUserInfoVo;
import com.nruonan.domain.vo.MenuVo;
import com.nruonan.domain.vo.RoutersVo;
import com.nruonan.domain.vo.UserInfoVo;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.service.LoginService;
import com.nruonan.service.MenuService;
import com.nruonan.service.RoleService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@RestController
public class LoginController {
    @Resource
    private LoginService loginService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) throws SystemException {
        if(!StringUtils.hasText(user.getUserName())){
            // 提示'必须要传用户名'。AppHttpCodeEnum是我们写的枚举类。SystemException是我们写的统一异常处理的类
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 根据id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //封装数据返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        // 获取当前登录的用户
        Long userId = SecurityUtils.getUserId();
        // 查询menu 结果是tree
        List<Menu> menus =menuService.selectRouterMenuTreeByUserId(userId);

        //封装数据
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
