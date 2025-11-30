package aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger("AUDIT");
    private final ObjectMapper mapper = new ObjectMapper();

    @Before("@annotation(aop.Auditable)")
    public void before(JoinPoint jp) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        Auditable aud = method.getAnnotation(Auditable.class);
        Map<String, Object> evt = new HashMap<>();
        evt.put("ts", Instant.now().toString());
        evt.put("phase", "START");
        evt.put("user", SecurityUtils.currentUser());
        evt.put("action", aud.action());
        evt.put("resource", aud.resource());
        evt.put("method", method.getDeclaringClass().getSimpleName() + "." + method.getName());
        evt.put("cid", MDC.get(CorrelationIdFilter.CORRELATION_ID));
        write(evt);
    }

    @AfterReturning(pointcut = "@annotation(aop.Auditable)", returning = "ret")
    public void after(JoinPoint jp, Object ret) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        Auditable aud = method.getAnnotation(Auditable.class);
        Map<String, Object> evt = new HashMap<>();
        evt.put("ts", Instant.now().toString());
        evt.put("phase", "END");
        evt.put("user", SecurityUtils.currentUser());
        evt.put("action", aud.action());
        evt.put("resource", aud.resource());
        evt.put("method", method.getDeclaringClass().getSimpleName() + "." + method.getName());
        evt.put("cid", MDC.get(CorrelationIdFilter.CORRELATION_ID));
        write(evt);
    }

    private void write(Map<String, Object> evt) {
        try {
            log.info(mapper.writeValueAsString(evt));
        } catch (Exception e) {
            log.info(evt.toString());
        }
    }
}