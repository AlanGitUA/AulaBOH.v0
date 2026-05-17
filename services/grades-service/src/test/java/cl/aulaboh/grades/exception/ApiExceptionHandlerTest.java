package cl.aulaboh.grades.exception;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {
    private final ApiExceptionHandler handler = new ApiExceptionHandler(new SimpleMeterRegistry());

    @Test
    void returnsServiceUnavailableWhenCircuitBreakerIsOpen() {
        var response = handler.circuitBreakerOpen(openCircuitBreakerException("grades"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertBody(response.getBody(), "CircuitBreakerOpen",
                "El servicio de calificaciones esta temporalmente no disponible. Intente nuevamente mas tarde.");
    }

    @Test
    void returnsBadRequestForBusinessException() {
        var response = handler.business(new BusinessException("La evaluacion no existe"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertBody(response.getBody(), "BusinessException", "La evaluacion no existe");
    }

    @Test
    void returnsServiceUnavailableForExternalDependency() {
        var response = handler.externalUnavailable(new ExternalServiceUnavailableException("students-service"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertBody(response.getBody(), "ExternalServiceUnavailableException",
                "students-service no esta disponible temporalmente.");
    }

    @Test
    void returnsInternalServerErrorForUnexpectedExceptionWithoutDetail() {
        var response = handler.general(new RuntimeException());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertBody(response.getBody(), "RuntimeException", "Sin detalle disponible");
    }

    private CallNotPermittedException openCircuitBreakerException(String name) {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(name);
        circuitBreaker.transitionToOpenState();
        return CallNotPermittedException.createCallNotPermittedException(circuitBreaker);
    }

    private void assertBody(Map<String, Object> body, String error, String message) {
        assertThat(body).isNotNull();
        assertThat(body).containsEntry("error", error).containsEntry("message", message);
        assertThat(body).containsKey("timestamp");
    }
}
