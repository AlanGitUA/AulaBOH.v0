package cl.aulaboh.grades;

import cl.aulaboh.grades.client.StudentClient;
import cl.aulaboh.grades.dto.StudentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = GradesServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GradeIntegrationTest {

    @Autowired MockMvc mockMvc;
    @MockBean StudentClient studentClient;

    private Long evalId;

    @BeforeEach
    void setUp() throws Exception {
        when(studentClient.findStudentById(anyLong()))
                .thenReturn(new StudentResponse(1L, "Ana", "Rojas", "1° Básico A", "ana@aulaboh.cl", "ACTIVE"));

        String evalBody = """
                {"subject":"Matemática","course":"1° Básico A","title":"Prueba Unidad 1","evaluationDate":"2026-05-08"}
                """;
        String response = mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON).content(evalBody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        evalId = Long.parseLong(response.replaceAll(".*\"id\":(\\d+).*", "$1"));
    }

    // ── Nota válida en escala chilena (1.0–7.0) → 201 ────────────────────
    @Test
    void registerGrade_validScore_returnsCreated() throws Exception {
        String body = String.format("{\"studentId\":1,\"evaluationId\":%d,\"score\":6.5}", evalId);
        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(6.5))
                .andExpect(jsonPath("$.studentId").value(1));
    }

    // ── REGLA DE NEGOCIO: nota > 7.0 → 400 ───────────────────────────────
    @Test
    void registerGrade_scoreTooHigh_returns400() throws Exception {
        String body = String.format("{\"studentId\":1,\"evaluationId\":%d,\"score\":7.1}", evalId);
        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // ── REGLA DE NEGOCIO: nota < 1.0 → 400 ───────────────────────────────
    @Test
    void registerGrade_scoreTooLow_returns400() throws Exception {
        String body = String.format("{\"studentId\":1,\"evaluationId\":%d,\"score\":0.9}", evalId);
        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // ── REGLA DE NEGOCIO: evaluación duplicada → error ───────────────────
    @Test
    void createEvaluation_duplicate_returns400() throws Exception {
        String body = """
                {"subject":"Matemática","course":"1° Básico A","title":"Prueba Unidad 1","evaluationDate":"2026-05-08"}
                """;
        mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError());
    }

    // ── Evaluación inexistente → error de negocio ─────────────────────────
    @Test
    void registerGrade_evaluationNotFound_returns400() throws Exception {
        String body = "{\"studentId\":1,\"evaluationId\":9999,\"score\":5.0}";
        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Listar notas del estudiante tras registro ─────────────────────────
    @Test
    void findByStudent_afterRegister_returnsGrades() throws Exception {
        String body = String.format("{\"studentId\":1,\"evaluationId\":%d,\"score\":5.5}", evalId);
        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/grades/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5.5))
                .andExpect(jsonPath("$[0].title").value("Prueba Unidad 1"));
    }
}
