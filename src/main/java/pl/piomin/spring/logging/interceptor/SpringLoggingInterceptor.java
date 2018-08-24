package pl.piomin.spring.logging.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

public class SpringLoggingInterceptor extends AbstractMonitoringInterceptor {

    @Value("${spring.logging.http.pattern.request}")
    String pattern;
    @Value("${spring.logging.http.pattern.response}")
    String patternResponse;
    @Value("${spring.logging.http.emptyPayloadPattern}")
    String emptyPayloadPattern;

    public SpringLoggingInterceptor() {
        setUseDynamicLogger(true);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation methodInvocation, Log log) throws Throwable {
        MDC.clear();
        long start = System.currentTimeMillis();
        ObjectMapper mapper = new ObjectMapper();
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        MDC.put("method", attrs.getRequest().getMethod());
        MDC.put("path", attrs.getRequest().getRequestURI());
        Integer indexOfBody = getBody(methodInvocation.getMethod());
        if (indexOfBody != null) {
            Object object = methodInvocation.getArguments()[indexOfBody];
            String msg = String.format(pattern, attrs.getRequest().getMethod(), attrs.getRequest().getRequestURI(), mapper.writeValueAsString(object));
            log.info(msg);
        } else {
            String msg = String.format(pattern, attrs.getRequest().getMethod(), attrs.getRequest().getRequestURI(), emptyPayloadPattern);
            log.info(msg);
        }

        Object objectRet = null;
        try {
            objectRet = methodInvocation.proceed();
            return objectRet;
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            String msg = String.format(patternResponse, attrs.getRequest().getMethod(), attrs.getRequest().getRequestURI(), mapper.writeValueAsString(objectRet));
            MDC.put("time", String.valueOf(time));
            log.info(msg);
        }
    }

    private Integer getBody(Method method) {
        int numberOfParameters = method.getParameterCount();
        for (int i = 0; i < numberOfParameters; i++) {
            int numberOfAnnotations = method.getParameterAnnotations()[i].length;
            for (int j = 0; j < numberOfAnnotations; j++) {
                if (method.getParameterAnnotations()[i][j] instanceof RequestBody) {
                    return i;
                }
            }
        }
        return null;
    }

}
