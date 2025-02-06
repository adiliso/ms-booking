package az.edu.turing.booking.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class CommonLoggingAspect {

    @Around("execution(* az.edu.turing.booking.controller..*(..))")
    public Object logEndpoints(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        String uri = "N/A";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            uri = request.getRequestURI();
        }

        final long start = System.currentTimeMillis();
        final Object result = joinPoint.proceed();
        final long elapsedTime = System.currentTimeMillis() - start;

        log.info("[Request] | URI: {} | {}.{}",
                uri, className, methodName);

        log.info("[Response] | URI: {} | {}.{} | Elapsed time: {} ms | Result: {}",
                uri, className, methodName, elapsedTime, result);

        return result;
    }
}
