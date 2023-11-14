package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.annotation.SystemLog;
import com.nruonan.domain.entity.User;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.service.BlogLoginService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Nruonan
 * @description
 */
@RestController
public class BlogLoginController {
    @Resource
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @SystemLog(businessName = "登录")
    public ResponseResult login(@RequestBody User user) throws SystemException {
        if(!StringUtils.hasText(user.getUserName())){
            // 提示'必须要传用户名'。AppHttpCodeEnum是我们写的枚举类。SystemException是我们写的统一异常处理的类
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }
    @SystemLog(businessName = "退出用户" )
    @PostMapping("/logout")
    public ResponseResult logout(){

        return blogLoginService.logout();
    }
}
