package com.www.log.aop.aspectj;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.www.log.aop.anno.OperateLog;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @Description AopLog
 * @Author 张卫刚
 * @Date Created on 2023/11/21
 */
@Aspect
@Component
@Slf4j
public class AopLog {

    private static final String UNKNOWN = "unknown";

    @Pointcut("execution(public * com.www.log.aop.controller.*Controller.*(..))")
    public void log() {

    }

    @Before(value = "log()")
    public void doBeforeLog(JoinPoint joinPoint) {
        log.info("aop-log: before");
    }

    @Around(value = "log()")
    public Object doAroundLog(ProceedingJoinPoint point) throws Throwable {
        //开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        //打印请求相关参数
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);

        final Log l = Log.builder()
                .threadId(Long.toString(Thread.currentThread().getId()))
                .threadName(String.format("%s.%s", point.getSignature().getDeclaringTypeName(), point.getSignature().getName()))
                .ip(getIp(request))
                .url(request.getRequestURL().toString())
                .classMethod(String.format("%s.%s", point.getSignature().getDeclaringTypeName(),
                        point.getSignature().getName()))
                .httpMethod(request.getMethod())
                .requestParams(getNameAndValue(point))
                .result(result)
                .timeCost(System.currentTimeMillis() - startTime)
                .userAgent(header)
                .browser(userAgent.getBrowser().toString())
                .os(userAgent.getOperatingSystem().toString())
                .build();

        log.info("Request Log Info : {}", JSONUtil.toJsonStr(l));
        return result;
    }

    @AfterReturning(pointcut = "log()")
    public void doAfterReturning(JoinPoint joinPoint) {
        log.info("aop-log: AfterReturning");
    }

    @AfterThrowing(value = "log()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info("aop-log: AfterThrowing");
    }


    @After(value = "log()")
    public void doAfter(JoinPoint point) {
        OperateLog operateLog = null;
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            operateLog = AnnotationUtils.findAnnotation(method,OperateLog.class);
        }
        if (operateLog != null) {
            log.info("aop-log: after");
        }
    }


    public Map<String, Object> getNameAndValue(ProceedingJoinPoint joinPoint) {
        final Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        final String[] names = methodSignature.getParameterNames();
        final Object[] args = joinPoint.getArgs();

        if (ArrayUtil.isEmpty(names) || ArrayUtil.isEmpty(args)) {
            return Collections.emptyMap();
        }
        if (names.length != args.length) {
            log.warn("{}方法参数名和参数值数量不一致", methodSignature.getName());
            return Collections.emptyMap();
        }
        Map<String, Object> map = Maps.newHashMap();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], args[i]);
        }
        return map;
    }


    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forward-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.equals(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            //获取本机真正的IP
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Log {
        // 线程id
        private String threadId;
        // 线程名称
        private String threadName;
        // ip
        private String ip;
        // url
        private String url;
        // http方法 GET POST PUT DELETE PATCH
        private String httpMethod;
        // 类方法
        private String classMethod;
        // 请求参数
        private Object requestParams;
        // 返回参数
        private Object result;
        // 接口耗时
        private Long timeCost;
        // 操作系统
        private String os;
        // 浏览器
        private String browser;
        // user-agent
        private String userAgent;
    }
}
