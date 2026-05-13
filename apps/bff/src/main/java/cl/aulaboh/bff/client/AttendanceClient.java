package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.*;
import cl.aulaboh.bff.exception.DownstreamServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "attendanceService", fallbackMethod = "createClassFallback")
    public ClassResponse createClass(ClassRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/classes", request, ClassResponse.class);
    }

    @CircuitBreaker(name = "attendanceService", fallbackMethod = "findClassesFallback")
    public ClassResponse[] findClasses() {
        return restTemplate.getForObject(baseUrl + "/api/classes", ClassResponse[].class);
    }

    @CircuitBreaker(name = "attendanceService", fallbackMethod = "registerAttendanceFallback")
    public AttendanceResponse registerAttendance(AttendanceRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/attendances", request, AttendanceResponse.class);
    }

    @CircuitBreaker(name = "attendanceService", fallbackMethod = "attendancesByStudentFallback")
    public AttendanceResponse[] attendancesByStudent(Long studentId) {
        return restTemplate.getForObject(baseUrl + "/api/attendances/student/" + studentId, AttendanceResponse[].class);
    }

    @CircuitBreaker(name = "attendanceService", fallbackMethod = "summaryFallback")
    public AttendanceSummaryResponse summary(Long studentId) {
        return restTemplate.getForObject(baseUrl + "/api/attendances/student/" + studentId + "/summary", AttendanceSummaryResponse.class);
    }

    private ClassResponse createClassFallback(ClassRequest request, Throwable ex) {
        throw new DownstreamServiceUnavailableException("attendance-service");
    }

    private ClassResponse[] findClassesFallback(Throwable ex) {
        return new ClassResponse[0];
    }

    private AttendanceResponse registerAttendanceFallback(AttendanceRequest request, Throwable ex) {
        throw new DownstreamServiceUnavailableException("attendance-service");
    }

    private AttendanceResponse[] attendancesByStudentFallback(Long studentId, Throwable ex) {
        return new AttendanceResponse[0];
    }

    private AttendanceSummaryResponse summaryFallback(Long studentId, Throwable ex) {
        return new AttendanceSummaryResponse(studentId, 0L, 0L, 0L);
    }
}
