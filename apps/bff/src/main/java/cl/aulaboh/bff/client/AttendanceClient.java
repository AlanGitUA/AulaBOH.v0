package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AttendanceClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AttendanceClient(RestTemplate restTemplate, @Value("${services.attendance.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ClassResponse createClass(ClassRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/classes", request, ClassResponse.class);
    }

    public ClassResponse[] findClasses() {
        return restTemplate.getForObject(baseUrl + "/api/classes", ClassResponse[].class);
    }

    public AttendanceResponse registerAttendance(AttendanceRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/attendances", request, AttendanceResponse.class);
    }

    public AttendanceResponse[] attendancesByStudent(Long studentId) {
        return restTemplate.getForObject(baseUrl + "/api/attendances/student/" + studentId, AttendanceResponse[].class);
    }

    public AttendanceSummaryResponse summary(Long studentId) {
        return restTemplate.getForObject(baseUrl + "/api/attendances/student/" + studentId + "/summary", AttendanceSummaryResponse.class);
    }
}
