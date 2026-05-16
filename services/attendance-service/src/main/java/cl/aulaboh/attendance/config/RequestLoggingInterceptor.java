package cl.aulaboh.attendance.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger("OPERACIONES");
    private static final String START_TIME = "requestStartTime";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String SERVICE_NAME = "attendance-service";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        request.setAttribute(START_TIME, System.currentTimeMillis());

        logger.info("""

                ------------------------------------------------------------
                OPERACION HTTP INICIADA
                Fecha y hora : {}
                Servicio     : {}
                Metodo HTTP  : {}
                Endpoint     : {}
                Origen       : {}
                ------------------------------------------------------------
                """,
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
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
                    Fecha y hora     : {}
                    Servicio         : {}
                    Metodo HTTP      : {}
                    Endpoint         : {}
                    Estado HTTP      : {}
                    Tiempo respuesta : {} ms
                    Resultado        : {}
                    ============================================================
                    """,
                    LocalDateTime.now().format(DATE_TIME_FORMATTER),
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
                    Fecha y hora     : {}
                    Servicio         : {}
                    Metodo HTTP      : {}
                    Endpoint         : {}
                    Estado HTTP      : {}
                    Tiempo respuesta : {} ms
                    Error            : {}
                    ============================================================
                    """,
                    LocalDateTime.now().format(DATE_TIME_FORMATTER),
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
