package com.simple.scheck.aspect;

import com.alibaba.fastjson.JSON;
import com.simple.scheck.annoation.OprLog;
import com.simple.scheck.dto.entity.OperationLog;
import com.simple.scheck.service.OprLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by dell on 2017/6/1.
 */
@Aspect
@Component
public class OprLogAspect {

    @Autowired
    private OprLogService oprLogService;

    //Controller层切点
    @Pointcut("@annotation(com.simple.scheck.annoation.OprLog)")
    public void controllerAspect() {
    }

    @Around(value="controllerAspect() && @annotation(oprLog)")
    public void insertOprLog(ProceedingJoinPoint jp, OprLog oprLog) throws Throwable{
        Object result = jp.proceed();
        String args = JSON.toJSONString(jp.getArgs());
        String res = JSON.toJSONString(result);
        OperationLog operationLog = new OperationLog();
        operationLog.setModuleName(oprLog.moduleName());
        operationLog.setOperationName(oprLog.option());
        operationLog.setParam(args);
        operationLog.setResult(res);
        operationLog.setUserId(1);
        operationLog.setUsername("System");
        operationLog.setOperationTime(new Date());
        oprLogService.insert(operationLog);
    }

//    第二种写法
//    @Around(value="controllerAspect()")
//    public void insertOprLog(ProceedingJoinPoint jp) {
//        MethodSignature signature = (MethodSignature) jp.getSignature();
//        Method method = signature.getMethod();
//        OprLog annotation = method.getAnnotation(OprLog.class);
//    }
}
