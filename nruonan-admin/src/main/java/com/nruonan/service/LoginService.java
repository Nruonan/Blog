package com.nruonan.service;

import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.User;

/**
 * @author Nruonan
 * @description
 */
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
