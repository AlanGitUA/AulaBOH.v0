package cl.aulaboh.attendance.client;

import cl.aulaboh.attendance.dto.StudentResponse;
import cl.aulaboh.attendance.exception.ExternalServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/** Adapter/Client Pattern: encapsula la comunicación con students-service. */
@Component
public class StudentClient {
    private static final Logger logger = LoggerFactory.getLogger(StudentClient.class);

    private final RestTemplate restTemplate;
    private final String studentsBaseUrl;

    public StudentClient(RestTemplate restTemplate, @Value("${services.students.url:http://localhost:8081}") String studentsBaseUrl) {
        this.restTemplate = restTemplate;
        this.studentsBaseUrl = studentsBaseUrl;
    }

    @CircuitBreaker(name = "studentsService", fallbackMethod = "findStudentByIdFallback")
    public StudentResponse findStudentById(Long studentId) {
        return restTemplate.getForObject(studentsBaseUrl + "/api/students/" + studentId, StudentResponse.class);
    }

    private StudentResponse findStudentByIdFallback(Long studentId, Throwable ex) {
        logger.warn("Circuit Breaker activado en attendance-service -> students-service | metodo=findStudentById | studentId={} | error={}", studentId, ex.getMessage(), ex);
        throw new ExternalServiceUnavailableException("students-service");
    }
}
