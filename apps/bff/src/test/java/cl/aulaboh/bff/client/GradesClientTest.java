package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.*;
import cl.aulaboh.bff.exception.DownstreamServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GradesClientTest {
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final GradesClient client = new GradesClient(restTemplate, "http://grades");

    @Test
    void delegatesSuccessfulRequestsToGradesService() {
        EvaluationRequest evaluationRequest = evaluationRequest();
        EvaluationResponse evaluation = new EvaluationResponse(1L, "1A", "Lenguaje", "Control 1", LocalDate.of(2026, 5, 17));
        GradeRequest gradeRequest = gradeRequest();
        GradeResponse grade = new GradeResponse(2L, 1L, "Lenguaje", "Control 1", 5L, 6.5);
        when(restTemplate.postForObject("http://grades/api/evaluations", evaluationRequest, EvaluationResponse.class)).thenReturn(evaluation);
        when(restTemplate.exchange(eq("http://grades/api/evaluations/1"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(EvaluationResponse.class)))
                .thenReturn(ResponseEntity.ok(evaluation));
        when(restTemplate.getForObject("http://grades/api/evaluations", EvaluationResponse[].class)).thenReturn(new EvaluationResponse[]{evaluation});
        when(restTemplate.postForObject("http://grades/api/grades", gradeRequest, GradeResponse.class)).thenReturn(grade);
        when(restTemplate.getForObject("http://grades/api/grades/student/5", GradeResponse[].class)).thenReturn(new GradeResponse[]{grade});

        assertThat(client.createEvaluation(evaluationRequest)).isEqualTo(evaluation);
        assertThat(client.updateEvaluation(1L, evaluationRequest)).isEqualTo(evaluation);
        assertThat(client.findEvaluations()).containsExactly(evaluation);
        assertThat(client.registerGrade(gradeRequest)).isEqualTo(grade);
        assertThat(client.gradesByStudent(5L)).containsExactly(grade);
    }

    @Test
    void exposesExpectedFallbackBehavior() {
        Throwable cause = new RuntimeException("down");

        assertThat((EvaluationResponse[]) ReflectionTestUtils.invokeMethod(client, "findEvaluationsFallback", cause)).isEmpty();
        assertThat((GradeResponse[]) ReflectionTestUtils.invokeMethod(client, "gradesByStudentFallback", 5L, cause)).isEmpty();
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "createEvaluationFallback", evaluationRequest(), cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "updateEvaluationFallback", 1L, evaluationRequest(), cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "registerGradeFallback", gradeRequest(), cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
    }

    private EvaluationRequest evaluationRequest() {
        EvaluationRequest request = new EvaluationRequest();
        request.setCourse("1A");
        request.setSubject("Lenguaje");
        request.setTitle("Control 1");
        request.setEvaluationDate(LocalDate.of(2026, 5, 17));
        return request;
    }

    private GradeRequest gradeRequest() {
        GradeRequest request = new GradeRequest();
        request.setEvaluationId(1L);
        request.setStudentId(5L);
        request.setScore(6.5);
        return request;
    }
}
