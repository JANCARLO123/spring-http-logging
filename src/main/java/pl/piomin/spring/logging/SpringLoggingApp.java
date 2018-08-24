package pl.piomin.spring.logging;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import pl.piomin.spring.logging.interceptor.SpringLoggingInterceptor;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringLoggingApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringLoggingApp.class, args);
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
