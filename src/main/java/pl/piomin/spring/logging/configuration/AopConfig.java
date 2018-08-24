package pl.piomin.spring.logging.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import pl.piomin.spring.logging.interceptor.SpringLoggingInterceptor;

//@Configuration
@Component
@Aspect
public class AopConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopConfig.class);

    //    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    //    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    @Pointcut("execution(* pl.piomin.spring.logging.controller..*(..))")
    public void monitor() {

    }

    @Around("execution(* pl.piomin.spring.logging.controller..*(..))")
    public Object profileExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        return result;
    }

}
