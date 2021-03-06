package tn.insat.pfe.searchservice.aop;


import net.logstash.logback.argument.StructuredArguments;
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

    @Around("execution(* tn.insat.pfe.searchservice..*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object proceed = joinPoint.proceed();
        long executionTime = System.nanoTime() - start;
        String logMsg =  "{} executed in  {}  ns";
        logger.info(logMsg ,
                StructuredArguments.value("method_signature",joinPoint.getSignature().toString()),
                StructuredArguments.value("execution_time", executionTime));
        return proceed;
    }

}
