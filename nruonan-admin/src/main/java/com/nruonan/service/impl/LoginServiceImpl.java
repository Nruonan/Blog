package com.nruonan.service.impl;

import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.LoginUser;
import com.nruonan.domain.entity.User;
import com.nruonan.domain.vo.BlogUserLoginVo;
import com.nruonan.domain.vo.UserInfoVo;
import com.nruonan.service.LoginService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.JwtUtil;
import com.nruonan.utils.RedisCache;
import com.nruonan.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Nruonan
 * @description
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        // 判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取userId 生成token
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(String.valueOf(userId));
        // 把用户信息存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        // 把token封装 返回
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login:" + userId);
        return ResponseResult.okResult();
    }
}
