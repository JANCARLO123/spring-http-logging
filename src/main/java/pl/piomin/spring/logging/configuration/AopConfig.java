package pl.piomin.spring.logging.configuration;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import pl.piomin.spring.logging.interceptor.SpringLoggingInterceptor;

@Configuration
@Aspect
@EnableAspectJAutoProxy
@EnableConfigurationProperties({SpringHttpLoggingConfig.class})
public class AopConfig {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void monitor() {

    }

    @Bean
    public SpringLoggingInterceptor springLoggingInterceptor() {
        return new SpringLoggingInterceptor();
    }


    @Bean
    public Advisor myPerformanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("pl.piomin.spring.logging.configuration.AopConfig.monitor()");
        return new DefaultPointcutAdvisor(pointcut, springLoggingInterceptor());
    }

}
