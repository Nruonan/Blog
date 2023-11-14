package com.nruonan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.LoginUser;
import com.nruonan.domain.entity.User;
import com.nruonan.domain.vo.BlogUserLoginVo;
import com.nruonan.domain.vo.UserInfoVo;
import com.nruonan.mapper.UserMapper;
import com.nruonan.service.BlogLoginService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.JwtUtil;
import com.nruonan.utils.RedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Nruonan
 * @description
 */
@Service
public class BlogLoginServiceImpl  implements BlogLoginService {

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
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        // 把token和userInfo封装 返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取token，然后解析token值获取其中的userid。SecurityContextHolder是security官方提供的类
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //LoginUser是我们写的类
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //获取userId
        Long userId = loginUser.getUser().getId();
        //在redis根据key来删除用户的value值，注意之前我们在存key的时候，key是加了'bloglogin:'前缀
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
