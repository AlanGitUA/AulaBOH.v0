package cl.aulaboh.attendance.client;

import cl.aulaboh.attendance.dto.StudentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/** Adapter/Client Pattern: encapsula la comunicación con students-service. */
@Component
public class StudentClient {
    private final RestTemplate restTemplate;
    private final String studentsBaseUrl;

    public StudentClient(RestTemplate restTemplate, @Value("${services.students.url:http://localhost:8081}") String studentsBaseUrl) {
        this.restTemplate = restTemplate;
        this.studentsBaseUrl = studentsBaseUrl;
    }

    public StudentResponse findStudentById(Long studentId) {
        return restTemplate.getForObject(studentsBaseUrl + "/api/students/" + studentId, StudentResponse.class);
    }
}
