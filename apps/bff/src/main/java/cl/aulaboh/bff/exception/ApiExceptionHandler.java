package cl.aulaboh.bff.exception;

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
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Map<String, Object>> downstream(HttpStatusCodeException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(errorBody("DownstreamServiceException", ex.getResponseBodyAsString()));
    }

    @ExceptionHandler(DownstreamServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> downstreamUnavailable(DownstreamServiceUnavailableException ex) {
        return ResponseEntity.status(503).body(errorBody("DownstreamServiceUnavailableException", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).orElse("Datos invalidos");
        return ResponseEntity.badRequest().body(errorBody("ValidationException", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> general(Exception ex) {
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
