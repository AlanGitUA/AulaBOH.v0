package cl.aulaboh.bff.exception;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<Map<String, Object>> circuitBreakerOpen(CallNotPermittedException ex) {
        logger.warn("Circuit Breaker abierto en BFF | breaker={} | error={}", ex.getCausingCircuitBreakerName(), ex.getMessage());
        return ResponseEntity.status(503).body(errorBody(
                "CircuitBreakerOpen",
                "El servicio esta temporalmente protegido por Circuit Breaker. Intente nuevamente mas tarde."
        ));
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Map<String, Object>> downstream(HttpStatusCodeException ex) {
        logger.warn("Error recibido desde microservicio downstream | status={} | body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
        return ResponseEntity.status(ex.getStatusCode()).body(errorBody("DownstreamServiceException", ex.getResponseBodyAsString()));
    }

    @ExceptionHandler(DownstreamServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> downstreamUnavailable(DownstreamServiceUnavailableException ex) {
        logger.warn("Microservicio downstream no disponible | error={}", ex.getMessage());
        return ResponseEntity.status(503).body(errorBody("DownstreamServiceUnavailableException", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).orElse("Datos invalidos");
        logger.warn("Error de validacion en BFF | detalle={}", message);
        return ResponseEntity.badRequest().body(errorBody("ValidationException", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> general(Exception ex) {
        logger.error("Error inesperado en BFF | tipo={} | mensaje={}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(errorBody(ex.getClass().getSimpleName(), ex.getMessage()));
    }

    private Map<String, Object> errorBody(String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("error", error == null ? "Error" : error);
        body.put("message", message == null ? "Sin detalle disponible" : message);
        return body;
    }
}
