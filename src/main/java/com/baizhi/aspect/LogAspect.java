package com.baizhi.aspect;

import com.baizhi.annotation.AddLog;
import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.Log;
import com.baizhi.entity.LogExample;
import com.baizhi.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Classname LogAspect
 * @Author GuOHuI
 * @Date 2020/12/24
 * @Time 21:46
 */
@Aspect //表示切面类
@Configuration //配置类  交给工厂管理
@Slf4j
public class LogAspect {
    @Resource
    HttpServletRequest request;

    @Resource
    LogMapper logMapper;
    //切增删改

    //@Around("execution( * com.baizhi.serviceImpl.*.*(..)) && !execution(* com.baizhi.serviceImpl.*.query*(..)) ")  切方法
    //@Around("within(com.baizhi.controller.SMSCodeController)") //切类
    @Around("@annotation(com.baizhi.annotation.AddLog)") //切注解
    public Object AddLog(ProceedingJoinPoint proceedingJoinPoint){

        log.info("--------进入环绕通知----------");
        //谁  时间  操作(哪个方法)   是否成功

        //获取管理员
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        
        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        //获取方法
        Method method = signature.getMethod();
        //通过类对象获取注解类
        AddLog addLog = method.getAnnotation(AddLog.class);
        //获取对应方法上对应注解的value属性的内容
        String methodDescription = addLog.value();

        Object result = null;
        String message = null;
        //放行方法  获取方法返回值
        try {
            result = proceedingJoinPoint.proceed();
            log.info("-----------执行目标方法-------------");
            message = "操作成功";
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            message = "操作失败";
        }


        log.info("--------返回环绕通知继续执行任务----------");


        //日志信息入库
        Log log = new Log(UUIDUtil.getUUID(),admin.getUsername(),new Date(),methodName+"("+methodDescription+")",message);

       logMapper.insertSelective(log);
        System.out.println("日志入库："+log);

        //返回值返回
        return result;
    }
}
