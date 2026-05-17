package cl.aulaboh.grades.controller;

import cl.aulaboh.grades.dto.EvaluationResponse;
import cl.aulaboh.grades.dto.GradeResponse;
import cl.aulaboh.grades.service.GradeService;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeController.class)
class GradeControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService service;

    @MockBean
    private MeterRegistry meterRegistry;

    @Test
    void createListAndUpdateEvaluations() throws Exception {
        EvaluationResponse evaluation = new EvaluationResponse(1L, "1A", "Matematica", "Prueba 1", LocalDate.of(2026, 5, 17));
        when(service.createEvaluation(org.mockito.ArgumentMatchers.any())).thenReturn(evaluation);
        when(service.findEvaluations("1A")).thenReturn(List.of(evaluation));
        when(service.updateEvaluation(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new EvaluationResponse(1L, "1A", "Matematica", "Prueba 2", LocalDate.of(2026, 5, 18)));

        mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "course": "1A",
                                  "subject": "Matematica",
                                  "title": "Prueba 1",
                                  "evaluationDate": "2026-05-17"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Prueba 1"));

        mockMvc.perform(get("/api/evaluations").param("course", "1A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].course").value("1A"));

        mockMvc.perform(put("/api/evaluations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "course": "1A",
                                  "subject": "Matematica",
                                  "title": "Prueba 2",
                                  "evaluationDate": "2026-05-18"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Prueba 2"));
    }

    @Test
    void createEvaluationRejectsInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "course": "",
                                  "subject": "",
                                  "title": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationException"));
    }

    @Test
    void registerAndListGrades() throws Exception {
        GradeResponse grade = new GradeResponse(10L, 1L, "Matematica", "Prueba 1", 5L, 6.5);
        when(service.registerGrade(org.mockito.ArgumentMatchers.any())).thenReturn(grade);
        when(service.findByStudent(5L)).thenReturn(List.of(grade));

        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "evaluationId": 1,
                                  "studentId": 5,
                                  "score": 6.5
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(6.5));

        mockMvc.perform(get("/api/grades/student/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(5L));
    }
}
