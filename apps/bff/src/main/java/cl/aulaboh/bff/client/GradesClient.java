package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.*;
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

    public EvaluationResponse createEvaluation(EvaluationRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/evaluations", request, EvaluationResponse.class);
    }

    public EvaluationResponse[] findEvaluations() {
        return restTemplate.getForObject(baseUrl + "/api/evaluations", EvaluationResponse[].class);
    }

    public GradeResponse registerGrade(GradeRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/grades", request, GradeResponse.class);
    }

    public GradeResponse[] gradesByStudent(Long studentId) {
        return restTemplate.getForObject(baseUrl + "/api/grades/student/" + studentId, GradeResponse[].class);
    }
}
