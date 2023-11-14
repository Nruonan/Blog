package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.annotation.SystemLog;
import com.nruonan.domain.entity.User;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/userInfo")
    @SystemLog(businessName = "查看用户信息")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("register")
    @SystemLog(businessName = "注册用户信息")
    public ResponseResult register(@RequestBody User user) throws SystemException {
        return  userService.register(user);
    }
}
