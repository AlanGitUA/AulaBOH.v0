package cl.aulaboh.attendance.exception;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private final MeterRegistry meterRegistry;

    public ApiExceptionHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<Map<String, Object>> circuitBreakerOpen(CallNotPermittedException ex) {
        logger.warn("Circuit Breaker abierto en attendance-service | breaker={} | error={}", ex.getCausingCircuitBreakerName(), ex.getMessage());
        recordError("CircuitBreakerOpen", 503);
        return ResponseEntity.status(503).body(errorBody(
                "CircuitBreakerOpen",
                "El servicio de asistencia esta temporalmente no disponible. Intente nuevamente mas tarde."
        ));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> business(BusinessException ex) {
        logger.warn("Error de negocio en attendance-service | error={}", ex.getMessage());
        recordError("BusinessException", 400);
        return ResponseEntity.badRequest().body(errorBody("BusinessException", ex.getMessage()));
    }

    @ExceptionHandler(ExternalServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> externalUnavailable(ExternalServiceUnavailableException ex) {
        logger.warn("Servicio externo no disponible desde attendance-service | error={}", ex.getMessage());
        recordError("ExternalServiceUnavailableException", 503);
        return ResponseEntity.status(503).body(errorBody("ExternalServiceUnavailableException", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Datos invalidos");
        logger.warn("Error de validacion en attendance-service | detalle={}", message);
        recordError("ValidationException", 400);
        return ResponseEntity.badRequest().body(errorBody("ValidationException", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> general(Exception ex) {
        logger.error("Error inesperado en attendance-service | tipo={} | mensaje={}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        recordError(ex.getClass().getSimpleName(), 500);
        return ResponseEntity.internalServerError().body(errorBody(ex.getClass().getSimpleName(), ex.getMessage()));
    }

    private void recordError(String type, int status) {
        Counter counter = Counter.builder("aulaboh.api.errors")
                .description("Errores HTTP manejados por la API")
                .tag("service", "attendance-service")
                .tag("type", type)
                .tag("status", String.valueOf(status))
                .register(meterRegistry);
        if (counter != null) {
            counter.increment();
        }
    }

    private Map<String, Object> errorBody(String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("error", error == null ? "Error" : error);
        body.put("message", message == null ? "Sin detalle disponible" : message);
        return body;
    }
}
