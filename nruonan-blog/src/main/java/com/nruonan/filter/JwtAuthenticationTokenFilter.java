package com.nruonan.filter;

import com.alibaba.fastjson.JSON;
import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.LoginUser;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.utils.JwtUtil;
import com.nruonan.utils.RedisCache;
import com.nruonan.utils.WebUtils;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Nruonan
 * @description
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
   @Autowired
   private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            // 不需要登录 直接放行
            filterChain.doFilter(request,response);
            return;
        }
        // 获取userid
        Claims claims = null;
        try {
           claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // token超时， 非法
            // 告诉前端需要重新登录
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        String userId = claims.getSubject();
        // 从redis中获取用户信息
        String key = "bloglogin:"+userId;
        LoginUser loginUser = redisCache.getCacheObject(key);
        //如果在redis获取不到值，说明登录是过期了，需要重新登录
        if(Objects.isNull(loginUser)){
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        // 存入securityContextHolder
        UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(token1);

        filterChain.doFilter(request,response);
    }
}
