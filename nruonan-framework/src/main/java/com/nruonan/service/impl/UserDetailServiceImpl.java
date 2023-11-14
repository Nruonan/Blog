package com.nruonan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.entity.LoginUser;
import com.nruonan.domain.entity.User;
import com.nruonan.mapper.MenuMapper;
import com.nruonan.mapper.UserMapper;
import com.nruonan.utils.BeanCopyUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        // 判断是否查到用户，如果没查到抛出异常
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        // 返回用户信息

        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> list = menuMapper.selectPermByUserId(user.getId());
            return  new LoginUser(user,list);
        }

        return  new LoginUser(user,null);
    }
}
