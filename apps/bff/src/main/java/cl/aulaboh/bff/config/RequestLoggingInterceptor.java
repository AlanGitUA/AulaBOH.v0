package cl.aulaboh.bff.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    private static final String START_TIME = "requestStartTime";
    private static final String SERVICE_NAME = "bff";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        request.setAttribute(START_TIME, System.currentTimeMillis());

        logger.info("""

                ------------------------------------------------------------
                OPERACION HTTP INICIADA
                Servicio    : {}
                Metodo HTTP : {}
                Endpoint    : {}
                Origen      : {}
                ------------------------------------------------------------
                """,
                SERVICE_NAME,
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr()
        );

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        Object startTimeAttribute = request.getAttribute(START_TIME);
        long startTime = startTimeAttribute instanceof Long ? (Long) startTimeAttribute : System.currentTimeMillis();
        long duration = System.currentTimeMillis() - startTime;

        String resultado = response.getStatus() >= 200 && response.getStatus() < 400
                ? "OK"
                : "ERROR";

        if (ex == null) {
            logger.info("""

                    ============================================================
                    OPERACION HTTP FINALIZADA
                    Servicio         : {}
                    Metodo HTTP      : {}
                    Endpoint         : {}
                    Estado HTTP      : {}
                    Tiempo respuesta : {} ms
                    Resultado        : {}
                    ============================================================
                    """,
                    SERVICE_NAME,
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    resultado
            );
        } else {
            logger.error("""

                    ============================================================
                    OPERACION HTTP CON ERROR
                    Servicio         : {}
                    Metodo HTTP      : {}
                    Endpoint         : {}
                    Estado HTTP      : {}
                    Tiempo respuesta : {} ms
                    Error            : {}
                    ============================================================
                    """,
                    SERVICE_NAME,
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    ex.getMessage(),
                    ex
            );
        }
    }
}
