package cl.aulaboh.bff.client;

import cl.aulaboh.bff.dto.*;
import cl.aulaboh.bff.exception.DownstreamServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttendanceClientTest {
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final AttendanceClient client = new AttendanceClient(restTemplate, "http://attendance");

    @Test
    void delegatesSuccessfulRequestsToAttendanceService() {
        ClassRequest classRequest = classRequest();
        AttendanceRequest attendanceRequest = attendanceRequest();
        ClassResponse classResponse = new ClassResponse(2L, "1A", "Matematica", LocalDate.of(2026, 5, 17));
        AttendanceResponse attendance = new AttendanceResponse(3L, 2L, "1A", "Matematica", LocalDate.of(2026, 5, 17), 5L, "PRESENT", null);
        AttendanceSummaryResponse summary = new AttendanceSummaryResponse(5L, 1L, 0L, 0L);
        when(restTemplate.postForObject("http://attendance/api/classes", classRequest, ClassResponse.class)).thenReturn(classResponse);
        when(restTemplate.getForObject("http://attendance/api/classes", ClassResponse[].class)).thenReturn(new ClassResponse[]{classResponse});
        when(restTemplate.postForObject("http://attendance/api/attendances", attendanceRequest, AttendanceResponse.class)).thenReturn(attendance);
        when(restTemplate.getForObject("http://attendance/api/attendances/student/5", AttendanceResponse[].class)).thenReturn(new AttendanceResponse[]{attendance});
        when(restTemplate.getForObject("http://attendance/api/attendances/student/5/summary", AttendanceSummaryResponse.class)).thenReturn(summary);

        assertThat(client.createClass(classRequest)).isEqualTo(classResponse);
        assertThat(client.findClasses()).containsExactly(classResponse);
        assertThat(client.registerAttendance(attendanceRequest)).isEqualTo(attendance);
        assertThat(client.attendancesByStudent(5L)).containsExactly(attendance);
        assertThat(client.summary(5L)).isEqualTo(summary);
    }

    @Test
    void exposesExpectedFallbackBehavior() {
        Throwable cause = new RuntimeException("down");

        assertThat((ClassResponse[]) ReflectionTestUtils.invokeMethod(client, "findClassesFallback", cause)).isEmpty();
        assertThat((AttendanceResponse[]) ReflectionTestUtils.invokeMethod(client, "attendancesByStudentFallback", 5L, cause)).isEmpty();
        assertThat((AttendanceSummaryResponse) ReflectionTestUtils.invokeMethod(client, "summaryFallback", 5L, cause))
                .isEqualTo(new AttendanceSummaryResponse(5L, 0L, 0L, 0L));
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "createClassFallback", classRequest(), cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(client, "registerAttendanceFallback", attendanceRequest(), cause))
                .isInstanceOf(DownstreamServiceUnavailableException.class);
    }

    private ClassRequest classRequest() {
        ClassRequest request = new ClassRequest();
        request.setCourse("1A");
        request.setSubject("Matematica");
        request.setClassDate(LocalDate.of(2026, 5, 17));
        return request;
    }

    private AttendanceRequest attendanceRequest() {
        AttendanceRequest request = new AttendanceRequest();
        request.setClassId(2L);
        request.setStudentId(5L);
        request.setStatus("PRESENT");
        return request;
    }
}
