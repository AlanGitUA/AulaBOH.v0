package cl.aulaboh.attendance;

import cl.aulaboh.attendance.client.StudentClient;
import cl.aulaboh.attendance.dto.StudentResponse;
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
@SpringBootTest(classes = AttendanceServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AttendanceIntegrationTest {

    @Autowired MockMvc mockMvc;

    // Corta la llamada HTTP real al students-service
    @MockBean StudentClient studentClient;

    private Long classId;

    @BeforeEach
    void setUp() throws Exception {
        when(studentClient.findStudentById(anyLong()))
                .thenReturn(new StudentResponse(1L, "Ana", "Rojas", "1° Básico A", "ana@aulaboh.cl", "ACTIVE"));

        String classBody = """
                {"subject":"Matemática","course":"1° Básico A","classDate":"2026-05-08"}
                """;
        String response = mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON).content(classBody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        classId = Long.parseLong(response.replaceAll(".*\"id\":(\\d+).*", "$1"));
    }

    // ── Registrar asistencia PRESENT → 201 ────────────────────────────────
    @Test
    void registerAttendance_present_returnsCreated() throws Exception {
        String body = String.format(
                "{\"studentId\":1,\"classId\":%d,\"status\":\"PRESENT\",\"observation\":\"Asiste puntual\"}", classId);
        mockMvc.perform(post("/api/attendances")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PRESENT"))
                .andExpect(jsonPath("$.studentId").value(1));
    }

    // ── REGLA DE NEGOCIO: no duplicados en misma clase ────────────────────
    @Test
    void registerAttendance_duplicate_returnsBusinessError() throws Exception {
        String body = String.format(
                "{\"studentId\":1,\"classId\":%d,\"status\":\"PRESENT\"}", classId);

        mockMvc.perform(post("/api/attendances")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        // Segunda vez: regla de negocio impide el duplicado
        mockMvc.perform(post("/api/attendances")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Clase inexistente → error de negocio ──────────────────────────────
    @Test
    void registerAttendance_classNotFound_returns400() throws Exception {
        String body = "{\"studentId\":1,\"classId\":9999,\"status\":\"ABSENT\"}";
        mockMvc.perform(post("/api/attendances")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Resumen refleja conteos correctos ─────────────────────────────────
    @Test
    void summary_afterMixedAttendances_returnsCorrectCounts() throws Exception {
        // Registrar PRESENT en clase 1
        mockMvc.perform(post("/api/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"studentId\":1,\"classId\":%d,\"status\":\"PRESENT\"}", classId)))
                .andExpect(status().isCreated());

        // Crear una segunda clase y registrar ABSENT
        String class2Body = """
                {"subject":"Historia","course":"1° Básico A","classDate":"2026-05-09"}
                """;
        String class2Response = mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON).content(class2Body))
                .andReturn().getResponse().getContentAsString();
        long class2Id = Long.parseLong(class2Response.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(post("/api/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"studentId\":1,\"classId\":%d,\"status\":\"ABSENT\"}", class2Id)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/attendances/student/1/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.present").value(1))
                .andExpect(jsonPath("$.absent").value(1))
                .andExpect(jsonPath("$.justified").value(0));
    }
}
