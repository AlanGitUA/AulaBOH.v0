package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.StudentRequest;
import cl.aulaboh.bff.dto.StudentResponse;
import cl.aulaboh.bff.exception.DownstreamServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StudentClient {
    private static final Logger logger = LoggerFactory.getLogger(StudentClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public StudentClient(RestTemplate restTemplate, @Value("${services.students.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @CircuitBreaker(name = "studentsService", fallbackMethod = "createFallback")
    public StudentResponse create(StudentRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/students", request, StudentResponse.class);
    }

    @CircuitBreaker(name = "studentsService", fallbackMethod = "findByIdFallback")
    public StudentResponse findById(Long id) {
        return restTemplate.getForObject(baseUrl + "/api/students/" + id, StudentResponse.class);
    }

    @CircuitBreaker(name = "studentsService", fallbackMethod = "findAllFallback")
    public StudentResponse[] findAll() {
        return restTemplate.getForObject(baseUrl + "/api/students", StudentResponse[].class);
    }

    @CircuitBreaker(name = "studentsService", fallbackMethod = "findByStudentUsernameFallback")
    public StudentResponse findByStudentUsername(String username) {
        return restTemplate.getForObject(baseUrl + "/api/students/username/" + username, StudentResponse.class);
    }

    @CircuitBreaker(name = "studentsService", fallbackMethod = "findByGuardianUsernameFallback")
    public StudentResponse[] findByGuardianUsername(String username) {
        return restTemplate.getForObject(baseUrl + "/api/students/guardian/" + username, StudentResponse[].class);
    }

    private StudentResponse createFallback(StudentRequest request, Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> students-service | metodo=create | error={}", ex.getMessage(), ex);
        throw new DownstreamServiceUnavailableException("students-service");
    }

    private StudentResponse findByIdFallback(Long id, Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> students-service | metodo=findById | id={} | error={}", id, ex.getMessage(), ex);
        throw new DownstreamServiceUnavailableException("students-service");
    }

    private StudentResponse[] findAllFallback(Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> students-service | metodo=findAll | respuesta degradada=[] | error={}", ex.getMessage(), ex);
        return new StudentResponse[0];
    }

    private StudentResponse findByStudentUsernameFallback(String username, Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> students-service | metodo=findByStudentUsername | username={} | error={}", username, ex.getMessage(), ex);
        throw new DownstreamServiceUnavailableException("students-service");
    }

    private StudentResponse[] findByGuardianUsernameFallback(String username, Throwable ex) {
        logger.warn("Circuit Breaker activado en BFF -> students-service | metodo=findByGuardianUsername | username={} | respuesta degradada=[] | error={}", username, ex.getMessage(), ex);
        return new StudentResponse[0];
    }
}
