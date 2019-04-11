package tn.insat.pfe.filemanagementservice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    @Around("execution(* tn.insat.pfe.filemanagementservice..*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object proceed = joinPoint.proceed();
        long executionTime = System.nanoTime() - start;
        String logMsg =  joinPoint.getSignature() + " executed in " + executionTime + " ns";
        logger.info(logMsg);
        return proceed;
    }

}
