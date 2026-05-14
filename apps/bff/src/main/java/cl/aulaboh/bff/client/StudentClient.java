package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.StudentRequest;
import cl.aulaboh.bff.dto.StudentResponse;
import cl.aulaboh.bff.exception.DownstreamServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StudentClient {
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

    private StudentResponse createFallback(StudentRequest request, Throwable ex) {
        throw new DownstreamServiceUnavailableException("students-service");
    }

    private StudentResponse findByIdFallback(Long id, Throwable ex) {
        throw new DownstreamServiceUnavailableException("students-service");
    }

    private StudentResponse[] findAllFallback(Throwable ex) {
        return new StudentResponse[0];
    }
}
