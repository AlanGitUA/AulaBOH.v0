package cl.aulaboh.attendance.controller;

import cl.aulaboh.attendance.dto.AttendanceResponse;
import cl.aulaboh.attendance.dto.AttendanceSummaryResponse;
import cl.aulaboh.attendance.dto.ClassResponse;
import cl.aulaboh.attendance.model.AttendanceStatus;
import cl.aulaboh.attendance.service.AttendanceService;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttendanceController.class)
class AttendanceControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendanceService service;

    @MockBean
    private MeterRegistry meterRegistry;

    @Test
    void createClassAndListClasses() throws Exception {
        when(service.createClass(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new ClassResponse(1L, "1A", "Matematica", LocalDate.of(2026, 5, 17)));
        when(service.findClasses("1A"))
                .thenReturn(List.of(new ClassResponse(1L, "1A", "Matematica", LocalDate.of(2026, 5, 17))));

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "course": "1A",
                                  "subject": "Matematica",
                                  "classDate": "2026-05-17"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        mockMvc.perform(get("/api/classes").param("course", "1A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subject").value("Matematica"));
    }

    @Test
    void createClassRejectsInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "course": "",
                                  "subject": "",
                                  "classDate": null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationException"));
    }

    @Test
    void registerAttendanceAndExposeQueries() throws Exception {
        AttendanceResponse response = new AttendanceResponse(
                10L, 1L, "1A", "Matematica", LocalDate.of(2026, 5, 17), 5L, AttendanceStatus.PRESENT, "OK"
        );
        when(service.registerAttendance(org.mockito.ArgumentMatchers.any())).thenReturn(response);
        when(service.findByStudent(5L)).thenReturn(List.of(response));
        when(service.findByCourse("1A")).thenReturn(List.of(response));
        when(service.getSummary(5L)).thenReturn(new AttendanceSummaryResponse(5L, 1, 0, 0));

        mockMvc.perform(post("/api/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "classId": 1,
                                  "studentId": 5,
                                  "status": "PRESENT",
                                  "observation": "OK"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(5L));

        mockMvc.perform(get("/api/attendances/student/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PRESENT"));

        mockMvc.perform(get("/api/attendances/course/1A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].course").value("1A"));

        mockMvc.perform(get("/api/attendances/student/5/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.present").value(1));

        verify(service).findByCourse("1A");
    }
}
