package cl.aulaboh.bff.exception;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {
    private final SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
    private final ApiExceptionHandler handler = new ApiExceptionHandler(meterRegistry);

    @Test
    void returnsServiceUnavailableWhenCircuitBreakerIsOpen() {
        var response = handler.circuitBreakerOpen(openCircuitBreakerException("bff"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertBody(response.getBody(), "CircuitBreakerOpen",
                "El servicio esta temporalmente protegido por Circuit Breaker. Intente nuevamente mas tarde.");
        assertErrorMetric("CircuitBreakerOpen", "503");
    }

    @Test
    void preservesDownstreamStatusAndBody() {
        var exception = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                null,
                "{\"error\":\"missing\"}".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        var response = handler.downstream(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertBody(response.getBody(), "DownstreamServiceException", "{\"error\":\"missing\"}");
        assertErrorMetric("DownstreamServiceException", "404");
    }

    @Test
    void returnsServiceUnavailableForUnavailableDownstream() {
        var response = handler.downstreamUnavailable(new DownstreamServiceUnavailableException("grades-service"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertBody(response.getBody(), "DownstreamServiceUnavailableException",
                "grades-service no esta disponible temporalmente.");
        assertErrorMetric("DownstreamServiceUnavailableException", "503");
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
                .tags("service", "bff", "type", type, "status", status)
                .counter()).isNotNull();
    }
}
