package com.nruonan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.UserDto;
import com.nruonan.domain.entity.User;
import com.nruonan.handler.exception.SystemException;

import java.util.List;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-11-08 10:03:39
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user) throws SystemException;

    ResponseResult Userslist(User user, Integer pageNum, Integer pageSize);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(User user);

    boolean checkEmailUnique(User user);

    ResponseResult addUser(User user);

    ResponseResult delete(List<Long> ids);

    ResponseResult updateUser(User user);
}
