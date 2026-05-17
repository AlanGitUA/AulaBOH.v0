package cl.aulaboh.students.exception;

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
        var response = handler.circuitBreakerOpen(openCircuitBreakerException("students"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertBody(response.getBody(), "CircuitBreakerOpen",
                "El servicio de estudiantes esta temporalmente no disponible. Intente nuevamente mas tarde.");
        assertErrorMetric("CircuitBreakerOpen", "503");
    }

    @Test
    void returnsNotFoundForMissingStudent() {
        var response = handler.notFound(new StudentNotFoundException(10L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertBody(response.getBody(), "StudentNotFoundException", "No existe un estudiante con id 10");
        assertErrorMetric("StudentNotFoundException", "404");
    }

    @Test
    void returnsNotFoundForMissingStudentUsername() {
        var response = handler.notFound(new StudentNotFoundException("estudiante.demo"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertBody(response.getBody(), "StudentNotFoundException",
                "No existe un estudiante asociado al usuario estudiante.demo");
        assertErrorMetric("StudentNotFoundException", "404");
    }

    @Test
    void returnsConflictForDuplicateStudentUsername() {
        var response = handler.duplicateUsername(new DuplicateStudentUsernameException("estudiante.demo"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertBody(response.getBody(), "DuplicateStudentUsernameException",
                "Ya existe un estudiante asociado al usuario estudiante.demo");
        assertErrorMetric("DuplicateStudentUsernameException", "409");
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
                .tags("service", "students-service", "type", type, "status", status)
                .counter()).isNotNull();
    }
}
