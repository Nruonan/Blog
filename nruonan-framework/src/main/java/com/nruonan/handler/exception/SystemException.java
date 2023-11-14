package com.nruonan.handler.exception;

import com.nruonan.enums.AppHttpCodeEnum;
import org.springframework.boot.ApplicationRunner;

/**
 * @author Nruonan
 * @description 自定义异常
 */
public class SystemException extends Exception{
    private int code;
    private String msg;
    public SystemException() {
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum){
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
