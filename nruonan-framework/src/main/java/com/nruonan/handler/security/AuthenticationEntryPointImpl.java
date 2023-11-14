package com.nruonan.handler.security;

import com.alibaba.fastjson.JSON;
import com.nruonan.ResponseResult;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nruonan
 * @description
 */
@Component
//自定义认证失败的处理器。
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //输出异常信息
        e.printStackTrace();
        ResponseResult result = null;
        //判断是登录才出现异常(返回'用户名或密码错误')，还是没有登录就访问特定接口才出现的异常(返回'需要登录后访问')，还是其它情况(返回'出现错误')
        if(e instanceof BadCredentialsException){
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR ,e.getMessage());
        }else if(e instanceof InsufficientAuthenticationException){
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            //第一个参数返回的是响应码，AppHttpCodeEnum是我们写的实体类。第二个参数是返回具体的信息
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");
        }
        // 响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
