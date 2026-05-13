package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.StudentRequest;
import cl.aulaboh.bff.dto.StudentResponse;
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

    public StudentResponse create(StudentRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/students", request, StudentResponse.class);
    }

    public StudentResponse findById(Long id) {
        return restTemplate.getForObject(baseUrl + "/api/students/" + id, StudentResponse.class);
    }

    public StudentResponse[] findAll() {
        return restTemplate.getForObject(baseUrl + "/api/students", StudentResponse[].class);
    }
}
