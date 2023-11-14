package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.User;

/**
 * @author Nruonan
 * @description
 */
public interface BlogLoginService  {
    ResponseResult login(User user);

    ResponseResult logout();
}
