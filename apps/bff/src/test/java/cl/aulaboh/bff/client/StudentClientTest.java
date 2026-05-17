package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.StudentRequest;
import cl.aulaboh.bff.dto.StudentResponse;
import cl.aulaboh.bff.exception.DownstreamServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    void delegatesSuccessfulRequestsToStudentsService() {
        StudentRequest request = request();
        StudentResponse student = student(1L);
        when(restTemplate.postForObject("http://students/api/students", request, StudentResponse.class)).thenReturn(student);
        when(restTemplate.exchange("http://students/api/students/1", HttpMethod.PUT, new HttpEntity<>(request), StudentResponse.class))
                .thenReturn(ResponseEntity.ok(student));
        when(restTemplate.getForObject("http://students/api/students/1", StudentResponse.class)).thenReturn(student);
        when(restTemplate.getForObject("http://students/api/students", StudentResponse[].class)).thenReturn(new StudentResponse[]{student});
        when(restTemplate.getForObject("http://students/api/students/username/estudiante.demo", StudentResponse.class)).thenReturn(student);
        when(restTemplate.getForObject("http://students/api/students/guardian/apoderado.demo", StudentResponse[].class)).thenReturn(new StudentResponse[]{student});

        assertThat(client.create(request)).isEqualTo(student);
        assertThat(client.update(1L, request)).isEqualTo(student);
        assertThat(client.findById(1L)).isEqualTo(student);
        assertThat(client.findAll()).containsExactly(student);
        assertThat(client.findByStudentUsername("estudiante.demo")).isEqualTo(student);
        assertThat(client.findByGuardianUsername("apoderado.demo")).containsExactly(student);
    }

    @Test
    void returnsDegradedArraysAndRaisesUnavailableErrorsFromFallbacks() {
        Throwable cause = new RuntimeException("down");

        assertThat((StudentResponse[]) ReflectionTestUtils.invokeMethod(client, "findAllFallback", cause)).isEmpty();
        assertThat((StudentResponse[]) ReflectionTestUtils.invokeMethod(client, "findByGuardianUsernameFallback", "apoderado.demo", cause)).isEmpty();
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "createFallback", request(), cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "updateFallback", 1L, request(), cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "deleteFallback", 1L, cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "findByIdFallback", 1L, cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "findByStudentUsernameFallback", "estudiante.demo", cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
    }

    private StudentRequest request() {
        StudentRequest request = new StudentRequest();
        request.setFirstName("Ana");
        request.setLastName("Rojas");
        request.setCourse("1A");
        request.setEmail("ana@aulaboh.cl");
        request.setStudentUsername("estudiante.demo");
        request.setGuardianUsername("apoderado.demo");
        return request;
    }

    private StudentResponse student(Long id) {
        return new StudentResponse(id, "Ana", "Rojas", "1A", "ana@aulaboh.cl", "estudiante.demo", "apoderado.demo", "ACTIVE");
    }
}
