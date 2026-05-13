package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.*;
import cl.aulaboh.bff.exception.DownstreamServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GradesClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public GradesClient(RestTemplate restTemplate, @Value("${services.grades.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @CircuitBreaker(name = "gradesService", fallbackMethod = "createEvaluationFallback")
    public EvaluationResponse createEvaluation(EvaluationRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/evaluations", request, EvaluationResponse.class);
    }

    @CircuitBreaker(name = "gradesService", fallbackMethod = "findEvaluationsFallback")
    public EvaluationResponse[] findEvaluations() {
        return restTemplate.getForObject(baseUrl + "/api/evaluations", EvaluationResponse[].class);
    }

    @CircuitBreaker(name = "gradesService", fallbackMethod = "registerGradeFallback")
    public GradeResponse registerGrade(GradeRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/grades", request, GradeResponse.class);
    }

    @CircuitBreaker(name = "gradesService", fallbackMethod = "gradesByStudentFallback")
    public GradeResponse[] gradesByStudent(Long studentId) {
        return restTemplate.getForObject(baseUrl + "/api/grades/student/" + studentId, GradeResponse[].class);
    }

    private EvaluationResponse createEvaluationFallback(EvaluationRequest request, Throwable ex) {
        throw new DownstreamServiceUnavailableException("grades-service");
    }

    private EvaluationResponse[] findEvaluationsFallback(Throwable ex) {
        return new EvaluationResponse[0];
    }

    private GradeResponse registerGradeFallback(GradeRequest request, Throwable ex) {
        throw new DownstreamServiceUnavailableException("grades-service");
    }

    private GradeResponse[] gradesByStudentFallback(Long studentId, Throwable ex) {
        return new GradeResponse[0];
    }
}
