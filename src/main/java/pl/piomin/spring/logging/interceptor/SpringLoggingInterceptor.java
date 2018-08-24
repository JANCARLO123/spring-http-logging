package pl.piomin.spring.logging.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.piomin.spring.logging.configuration.SpringHttpLoggingConfig;

import java.lang.reflect.Method;

public class SpringLoggingInterceptor extends AbstractMonitoringInterceptor {

    @Autowired
    SpringHttpLoggingConfig config;

    public SpringLoggingInterceptor() {
        setUseDynamicLogger(true);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation methodInvocation, Log log) throws Throwable {
        MDC.clear();
        final long start = System.currentTimeMillis();
        final ObjectMapper mapper = new ObjectMapper();
        final ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        MDC.put("method", attrs.getRequest().getMethod());
        MDC.put("path", attrs.getRequest().getRequestURI());

        String msg = null;
        final Integer indexOfBody = getBody(methodInvocation.getMethod());
        if (indexOfBody != null) {
            Object object = methodInvocation.getArguments()[indexOfBody];
            msg = String.format(config.getPatternRequest(), attrs.getRequest().getMethod(), attrs.getRequest().getRequestURI(), mapper.writeValueAsString(object));
        } else {
            msg = String.format(config.getPatternRequest(), attrs.getRequest().getMethod(), attrs.getRequest().getRequestURI(), config.getEmptyPayloadPattern());
        }
        log.info(msg);

        String[] includeHeadersTab = config.getIncludeHeaders().split(",");
        for (int i = 0; i < includeHeadersTab.length; i++) {
            String headerValue = attrs.getRequest().getHeader(includeHeadersTab[i]);
            MDC.put(includeHeadersTab[i], headerValue);
        }

        Object objectRet = null;
        try {
            objectRet = methodInvocation.proceed();
            return objectRet;
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            MDC.put("time", String.valueOf(time));
            MDC.put("status", String.valueOf(attrs.getResponse().getStatus()));

            for (int i = 0; i < includeHeadersTab.length; i++) {
                attrs.getResponse().setHeader(includeHeadersTab[i], MDC.get(includeHeadersTab[i]));
            }

            if (objectRet != null)
                msg = String.format(config.getPatternResponse(), attrs.getRequest().getMethod(), attrs.getRequest().getRequestURI(), mapper.writeValueAsString(objectRet));
            else
                msg = String.format(config.getPatternResponse(), attrs.getRequest().getMethod(), attrs.getRequest().getRequestURI(), config.getEmptyPayloadPattern());
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
