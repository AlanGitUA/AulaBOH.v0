package cl.aulaboh.bff.controller;

import cl.aulaboh.bff.client.AttendanceClient;
import cl.aulaboh.bff.client.GradesClient;
import cl.aulaboh.bff.config.SecurityConfig;
import cl.aulaboh.bff.dto.AttendanceResponse;
import cl.aulaboh.bff.dto.ClassResponse;
import cl.aulaboh.bff.dto.EvaluationResponse;
import cl.aulaboh.bff.dto.GradeResponse;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AcademicDataController.class)
@Import(SecurityConfig.class)
class AcademicDataControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendanceClient attendanceClient;

    @MockBean
    private GradesClient gradesClient;

    @MockBean
    private MeterRegistry meterRegistry;

    @Test
    void createsAndListsClasses() throws Exception {
        when(attendanceClient.createClass(any()))
                .thenReturn(new ClassResponse(1L, "1A", "Matematica", LocalDate.of(2026, 5, 17)));
        when(attendanceClient.findClasses())
                .thenReturn(new ClassResponse[]{new ClassResponse(1L, "1A", "Matematica", LocalDate.of(2026, 5, 17))});

        mockMvc.perform(post("/api/bff/classes")
                        .with(teacherJwt())
                        .contentType("application/json")
                        .content("""
                                {
                                  "course":"1A",
                                  "subject":"Matematica",
                                  "classDate":"2026-05-17"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subject").value("Matematica"));

        mockMvc.perform(get("/api/bff/classes").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].course").value("1A"));
    }

    @Test
    void registersAndListsAttendances() throws Exception {
        when(attendanceClient.registerAttendance(any()))
                .thenReturn(new AttendanceResponse(
                        1L, 2L, "1A", "Matematica", LocalDate.of(2026, 5, 17),
                        5L, "PRESENT", null
                ));
        when(attendanceClient.attendancesByStudent(5L))
                .thenReturn(new AttendanceResponse[]{
                        new AttendanceResponse(
                                1L, 2L, "1A", "Matematica", LocalDate.of(2026, 5, 17),
                                5L, "PRESENT", null
                        )
                });

        mockMvc.perform(post("/api/bff/attendances")
                        .with(teacherJwt())
                        .contentType("application/json")
                        .content("""
                                {
                                  "classId":2,
                                  "studentId":5,
                                  "status":"PRESENT"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(5));

        mockMvc.perform(get("/api/bff/attendances/student/5").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PRESENT"));
    }

    @Test
    void createsUpdatesAndListsEvaluations() throws Exception {
        when(gradesClient.createEvaluation(any()))
                .thenReturn(new EvaluationResponse(1L, "1A", "Lenguaje", "Control 1", LocalDate.of(2026, 5, 17)));
        when(gradesClient.updateEvaluation(eq(1L), any()))
                .thenReturn(new EvaluationResponse(1L, "1A", "Lenguaje", "Control actualizado", LocalDate.of(2026, 5, 18)));
        when(gradesClient.findEvaluations())
                .thenReturn(new EvaluationResponse[]{
                        new EvaluationResponse(1L, "1A", "Lenguaje", "Control actualizado", LocalDate.of(2026, 5, 18))
                });

        mockMvc.perform(post("/api/bff/evaluations")
                        .with(teacherJwt())
                        .contentType("application/json")
                        .content("""
                                {
                                  "course":"1A",
                                  "subject":"Lenguaje",
                                  "title":"Control 1",
                                  "evaluationDate":"2026-05-17"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Control 1"));

        mockMvc.perform(put("/api/bff/evaluations/1")
                        .with(teacherJwt())
                        .contentType("application/json")
                        .content("""
                                {
                                  "course":"1A",
                                  "subject":"Lenguaje",
                                  "title":"Control actualizado",
                                  "evaluationDate":"2026-05-18"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Control actualizado"));

        mockMvc.perform(get("/api/bff/evaluations").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].evaluationDate").value("2026-05-18"));
    }

    @Test
    void registersGradeAndReturnsEmptyListsWhenClientsReturnNull() throws Exception {
        when(gradesClient.registerGrade(any()))
                .thenReturn(new GradeResponse(1L, 3L, "Lenguaje", "Control 1", 5L, 6.5));
        when(attendanceClient.findClasses()).thenReturn(null);
        when(attendanceClient.attendancesByStudent(5L)).thenReturn(null);
        when(gradesClient.findEvaluations()).thenReturn(null);

        mockMvc.perform(post("/api/bff/grades")
                        .with(teacherJwt())
                        .contentType("application/json")
                        .content("""
                                {
                                  "evaluationId":3,
                                  "studentId":5,
                                  "score":6.5
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(6.5));

        mockMvc.perform(get("/api/bff/classes").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/api/bff/attendances/student/5").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/api/bff/evaluations").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    private org.springframework.test.web.servlet.request.RequestPostProcessor teacherJwt() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_DOCENTE"));
    }
}
