package com.nruonan.handler.security;

import com.nruonan.ResponseResult;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.handler.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Nruonan
 * @description
 */
@ControllerAdvice
@ResponseBody
//使用Lombok提供的Slf4j注解，实现日志功能
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SystemException.class)//SystemException是我们写的类。用户登录的异常交给这里处理
    public ResponseResult systemException(SystemException e){
        // 打印异常信息
        e.printStackTrace();

        log.error("出现了异常!  " , e);
        //从异常对象中获取提示信息封装，然后返回。ResponseResult是我们写的类
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult systemException(Exception e){
        // 打印异常信息
        e.printStackTrace();

        log.error("出现了异常!  " , e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,e.getMessage());
    }
}
