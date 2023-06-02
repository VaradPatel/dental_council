package in.gov.abdm.nmr.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {


    public static final String EXITING_WITH_ARGUMENT = "Exiting: {}.{}()";

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(in.gov.abdm.nmr.controller..*)"
            +" || within(in.gov.abdm.nmr.service.impl..*)")
    public void logPointcut() {
    }

     /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("Entering: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            log.info(EXITING_WITH_ARGUMENT, joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            return  result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
