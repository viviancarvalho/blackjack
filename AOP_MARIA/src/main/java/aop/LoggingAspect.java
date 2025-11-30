package aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* *..service..*(..)) || execution(* *..controller..*(..))")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String method = pjp.getSignature().toShortString();
        String args = Arrays.toString(pjp.getArgs());
        String cid = MDC.get(CorrelationIdFilter.CORRELATION_ID);
        log.info("IN method={} cid={} args={}", method, cid, args);
        try {
            Object result = pjp.proceed();
            long ms = System.currentTimeMillis() - start;
            log.info("OUT method={} cid={} ms={}", method, cid, ms);
            return result;
        } catch (Throwable ex) {
            long ms = System.currentTimeMillis() - start;
            log.error("ERR method={} cid={} ms={} ex={}", method, cid, ms, ex.toString());
            throw ex;
        }
    }
}