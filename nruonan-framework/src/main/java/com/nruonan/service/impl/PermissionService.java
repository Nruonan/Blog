package com.nruonan.service.impl;

import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.entity.LoginUser;
import com.nruonan.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@Service("ps")
public class PermissionService {
    /**
     * 判断用户是否具有permission权限
     */
    public boolean hasPermission(String permission){
        // 如果是超级管理员 id为1直接返回true
        if (SecurityUtils.getUserId().equals(1L)) {
            return true;
        }
        // 否则 获取当前用户所具有的权限
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<String> permissions = loginUser.getPermissions();
        return permissions.contains(permission);
    }
}
