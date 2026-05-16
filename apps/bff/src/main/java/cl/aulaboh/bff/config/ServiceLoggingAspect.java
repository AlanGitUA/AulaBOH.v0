package cl.aulaboh.bff.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Around("execution(* cl.aulaboh.bff.facade..*(..)) || execution(* cl.aulaboh.bff.client..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.info("Iniciando metodo: {}.{}", className, methodName);

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Metodo finalizado: {}.{} | Tiempo de respuesta: {} ms", className, methodName, duration);
            return result;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Error en metodo: {}.{} | Tiempo antes del error: {} ms | Error: {}",
                    className, methodName, duration, ex.getMessage(), ex);
            throw ex;
        }
    }
}
