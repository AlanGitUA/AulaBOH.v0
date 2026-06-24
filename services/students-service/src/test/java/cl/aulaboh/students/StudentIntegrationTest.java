package cl.aulaboh.students;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = StudentsServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentIntegrationTest {

    @Autowired MockMvc mockMvc;

    private static final String VALID_BODY = """
            {"firstName":"Ana","lastName":"Rojas","course":"1° Básico A","email":"ana@aulaboh.cl","birthDate":"2015-05-10"}
            """;

    // ── Crear estudiante válido → 201 + status ACTIVE ─────────────────────
    @Test
    void createStudent_validRequest_returnsCreatedAndActive() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Ana"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    // ── Curso con formato inválido → 400 ──────────────────────────────────
    @Test
    void createStudent_invalidCourseFormat_returns400() throws Exception {
        String body = """
                {"firstName":"Ana","lastName":"Rojas","course":"5to Año","email":"ana@aulaboh.cl"}
                """;
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Nombre en blanco → 400 ─────────────────────────────────────────────
    @Test
    void createStudent_missingFirstName_returns400() throws Exception {
        String body = """
                {"lastName":"Rojas","course":"1° Básico A","email":"ana@aulaboh.cl"}
                """;
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Estudiante no encontrado → 404 ────────────────────────────────────
    @Test
    void findById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound());
    }

    // ── Crear + buscar por ID (flujo integración real) ────────────────────
    @Test
    void createThenFindById_returnsCorrectStudent() throws Exception {
        String response = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = Long.parseLong(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(get("/api/students/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Rojas"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    // ── REGLA DE NEGOCIO: username duplicado → 409 ────────────────────────
    @Test
    void createStudent_duplicateUsername_returns409() throws Exception {
        String body = """
                {"firstName":"Ana","lastName":"Rojas","course":"1° Básico A",
                 "email":"ana@aulaboh.cl","studentUsername":"ana.rojas"}
                """;
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError());
    }

    // ── Borrado lógico: estudiante queda INACTIVE ─────────────────────────
    @Test
    void deleteStudent_marksAsInactive() throws Exception {
        String response = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andReturn().getResponse().getContentAsString();

        long id = Long.parseLong(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(delete("/api/students/" + id))
                .andExpect(status().isNoContent());

        // El registro sigue en BD pero no aparece en findAll (solo ACTIVE)
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
