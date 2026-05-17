package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.*;
import cl.aulaboh.bff.exception.DownstreamServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GradesClient {
    private static final Logger logger = LoggerFactory.getLogger(GradesClient.class);

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

    @CircuitBreaker(name = "gradesService", fallbackMethod = "updateEvaluationFallback")
    public EvaluationResponse updateEvaluation(Long evaluationId, EvaluationRequest request) {
        return restTemplate.exchange(
                baseUrl + "/api/evaluations/" + evaluationId,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                EvaluationResponse.class
        ).getBody();
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
        logger.warn("Circuit Breaker activado en BFF -> grades-service | metodo=createEvaluation | error={}", ex.getMessage(), ex);
        throw new DownstreamServiceUnavailableException("grades-service");
    }

    private EvaluationResponse[] findEvaluationsFallback(Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> grades-service | metodo=findEvaluations | respuesta degradada=[] | error={}", ex.getMessage(), ex);
        return new EvaluationResponse[0];
    }

    private EvaluationResponse updateEvaluationFallback(Long evaluationId, EvaluationRequest request, Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> grades-service | metodo=updateEvaluation | evaluationId={} | error={}", evaluationId, ex.getMessage(), ex);
        throw new DownstreamServiceUnavailableException("grades-service");
    }

    private GradeResponse registerGradeFallback(GradeRequest request, Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> grades-service | metodo=registerGrade | error={}", ex.getMessage(), ex);
        throw new DownstreamServiceUnavailableException("grades-service");
    }

    private GradeResponse[] gradesByStudentFallback(Long studentId, Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> grades-service | metodo=gradesByStudent | studentId={} | respuesta degradada=[] | error={}", studentId, ex.getMessage(), ex);
        return new GradeResponse[0];
    }
}
