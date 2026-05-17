package cl.aulaboh.grades.client;

import cl.aulaboh.grades.dto.StudentResponse;
import cl.aulaboh.grades.exception.ExternalServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentClientTest {
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final StudentClient client = new StudentClient(restTemplate, "http://students");

    @Test
    void delegatesStudentLookup() {
        StudentResponse response = new StudentResponse(5L, "Ana", "Rojas", "1A", "ana@aulaboh.cl", "ACTIVE");
        when(restTemplate.getForObject("http://students/api/students/5", StudentResponse.class)).thenReturn(response);

        assertThat(client.findStudentById(5L)).isEqualTo(response);
    }

    @Test
    void raisesUnavailableErrorFromFallback() {
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "findStudentByIdFallback", 5L, new RuntimeException("down")))
                .isInstanceOf(ExternalServiceUnavailableException.class);
    }
}
