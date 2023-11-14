package com.nruonan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.nruonan.domain.entity.UserRole;
import com.nruonan.mapper.UserRoleMapper;
import com.nruonan.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author 35238
 * @date 2023/8/10 0010 20:42
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}