package cl.aulaboh.grades.exception;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {
    private final SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
    private final ApiExceptionHandler handler = new ApiExceptionHandler(meterRegistry);

    @Test
    void returnsServiceUnavailableWhenCircuitBreakerIsOpen() {
        var response = handler.circuitBreakerOpen(openCircuitBreakerException("grades"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertBody(response.getBody(), "CircuitBreakerOpen",
                "El servicio de calificaciones esta temporalmente no disponible. Intente nuevamente mas tarde.");
        assertErrorMetric("CircuitBreakerOpen", "503");
    }

    @Test
    void returnsBadRequestForBusinessException() {
        var response = handler.business(new BusinessException("La evaluacion no existe"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertBody(response.getBody(), "BusinessException", "La evaluacion no existe");
        assertErrorMetric("BusinessException", "400");
    }

    @Test
    void returnsServiceUnavailableForExternalDependency() {
        var response = handler.externalUnavailable(new ExternalServiceUnavailableException("students-service"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertBody(response.getBody(), "ExternalServiceUnavailableException",
                "students-service no esta disponible temporalmente.");
        assertErrorMetric("ExternalServiceUnavailableException", "503");
    }

    @Test
    void returnsInternalServerErrorForUnexpectedExceptionWithoutDetail() {
        var response = handler.general(new RuntimeException());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertBody(response.getBody(), "RuntimeException", "Sin detalle disponible");
        assertErrorMetric("RuntimeException", "500");
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

    private void assertErrorMetric(String type, String status) {
        assertThat(meterRegistry.find("aulaboh.api.errors")
                .tags("service", "grades-service", "type", type, "status", status)
                .counter()).isNotNull();
    }
}
