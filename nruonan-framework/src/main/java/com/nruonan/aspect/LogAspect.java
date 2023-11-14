package com.nruonan.aspect;

import com.alibaba.fastjson.JSON;
import com.nruonan.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Nruonan
 * @description
 */
@Component
@Aspect // aop
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.nruonan.annotation.SystemLog)") // 需要标志SystemLog则使用环绕
    //确定哪个切点，以后哪个类想成为切点，就在哪个类添加上面那行的注解。例如下面这个pt()就是切点
    public void pt(){
    }

    @Around("pt()")
    //ProceedingJoinPoint可以拿到被增强方法的信息
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //proceed方法表示调用目标方法，preceed就是目标方法执行完之后的返回值
        Object proceed;
        try {
            handleBefore(joinPoint);
            proceed = joinPoint.proceed();//这是目标方法执行完成，上一行是目标方法未执行，下一行是目标方法已经执行
            //调用下面写的'实现打印日志信息的数据信息'的方法
            handlerAfter(proceed);
        }finally{
            // 结束后换行
            log.info("=======================end=======================" + System.lineSeparator());
        }
        return proceed;
    }

    private void handlerAfter(Object proceed) {
        // 打印请求出参
        log.info("传出参数   : {}", JSON.toJSONString(proceed));
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //ServletRequestAttributes是RequestAttributes是spring接口的实现类
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        //下面那行就可以拿到请求的报文了，其中有我们需要的url、请求方式、ip。这里拿到的request会在下面的log中大量使用
        HttpServletRequest request = requestAttributes.getRequest();

        // 获取被增强方法的注解对象
        SystemLog systemLog =  getSystemLog(joinPoint);
        log.info("======================Start======================");

        // 打印请求 URL
        log.info("请求URL   : {}",request.getRequestURL());
        // 打印描述信息

        log.info("接口描述   : {}", systemLog.businessName());

        // 打印 Http method
        log.info("请求方式   : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("请求类名   : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) joinPoint.getSignature()).getName() );
        // 打印请求的 IP
        log.info("访问IP    : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("传入参数   : {}", JSON.toJSONString(joinPoint.getArgs()));

    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        //Signature是spring提供的接口，MethodSignature是Signature的子接口
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        //Systemlog是我们写的自定义注解的接口
        //下面那行就能获取被增强方法的注解对象，例如获取UserController类的updateUserInfo方法上一行的@mySystemlog注解
        SystemLog systemLog = signature.getMethod().getAnnotation(SystemLog.class);

        return systemLog;
    }

}
