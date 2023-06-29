package com.qinghua.website.api.config;

import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.common.SessionUser;
import com.qinghua.website.server.domain.LogInfoDTO;
import com.qinghua.website.server.service.LogInfoService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 业务日志AOP 切面类
 */
@Component
@Aspect
public class LogAspect {

    @Autowired
    private LogInfoService logInfoService;

    @Pointcut("@annotation(com.qinghua.website.api.annotation.LogAnnotation)")
    public void pointCut(){

    }

    @Before("pointCut()")
    public void sleep(){

    }

    @After("pointCut()")
    public void addLog(JoinPoint joinPoint) throws UnknownHostException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Class<?> aClass = joinPoint.getTarget().getClass();
        String name  =joinPoint.getSignature().getName();
        LogInfoDTO log = new LogInfoDTO();
        InetAddress address = InetAddress.getLocalHost();
        log.setIp(address.getHostAddress());
        SessionUser user = (SessionUser) session.getAttribute(SessionUser.SEESION_USER);
        if(null != user){
            log.setUserName(user.getUserName());
        }else{
            log.setUserName("未登录用户");
        }
        Method[] methods = aClass.getMethods();
        Object[] argumets = joinPoint.getArgs();
        for (Method method:methods) {
            String methodName = method.getName();
            Class[] clazzs = method.getParameterTypes();
            if (methodName.equals(name) && clazzs.length == argumets.length){//方法同名相同进入
                LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
                String logType = annotation.logType();
                String logDesc = annotation.logDesc();
                log.setType(logType);
                log.setContent(logDesc);
            }
        }
        logInfoService.saveLogInfo(log);
    }

}
