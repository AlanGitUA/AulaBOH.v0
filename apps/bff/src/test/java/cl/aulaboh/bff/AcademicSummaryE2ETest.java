package cl.aulaboh.bff;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

/**
 * E2E Test: flujo completo Cliente → BFF → [students | attendance | grades]
 * Los 3 microservicios downstream son simulados con WireMock.
 * Spring Security se desactiva via TestSecurityConfig.
 */
@ActiveProfiles("test")
@SpringBootTest(
        classes = {BffApplication.class, AcademicSummaryE2ETest.TestSecurityConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AcademicSummaryE2ETest {

    @LocalServerPort int port;

    static WireMockServer studentsWM   = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
    static WireMockServer attendanceWM = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
    static WireMockServer gradesWM     = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

    @BeforeAll
    static void startWireMocks() {
        studentsWM.start();
        attendanceWM.start();
        gradesWM.start();
    }

    @AfterAll
    static void stopWireMocks() {
        studentsWM.stop();
        attendanceWM.stop();
        gradesWM.stop();
    }

    @DynamicPropertySource
    static void overrideUrls(DynamicPropertyRegistry registry) {
        registry.add("services.students.url",   () -> "http://localhost:" + studentsWM.port());
        registry.add("services.attendance.url", () -> "http://localhost:" + attendanceWM.port());
        registry.add("services.grades.url",     () -> "http://localhost:" + gradesWM.port());
        registry.add("eureka.client.enabled",   () -> "false");
    }

    @BeforeEach
    void resetAndSetPort() {
        studentsWM.resetAll();
        attendanceWM.resetAll();
        gradesWM.resetAll();
        RestAssured.port = port;
    }

    // ── Stubs helpers ─────────────────────────────────────────────────────

    private void stubStudentById(long id) {
        studentsWM.stubFor(get(urlEqualTo("/api/students/" + id))
                .willReturn(okJson(String.format(
                        "{\"id\":%d,\"firstName\":\"Ana\",\"lastName\":\"Rojas\"," +
                        "\"course\":\"1° Básico A\",\"email\":\"ana@aulaboh.cl\"," +
                        "\"studentUsername\":\"ana.rojas\",\"guardianUsername\":\"apoderado.rojas\"," +
                        "\"status\":\"ACTIVE\"}", id))));
    }

    private void stubStudentsList() {
        studentsWM.stubFor(get(urlEqualTo("/api/students"))
                .willReturn(okJson("[{\"id\":1,\"firstName\":\"Ana\",\"lastName\":\"Rojas\"," +
                        "\"course\":\"1° Básico A\",\"email\":\"ana@aulaboh.cl\"," +
                        "\"studentUsername\":\"ana.rojas\",\"guardianUsername\":\"apoderado.rojas\"," +
                        "\"status\":\"ACTIVE\"}]")));
    }

    private void stubStudentCreate() {
        studentsWM.stubFor(post(urlEqualTo("/api/students"))
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"firstName\":\"Ana\",\"lastName\":\"Rojas\"," +
                                "\"course\":\"1° Básico A\",\"email\":\"ana@aulaboh.cl\"," +
                                "\"studentUsername\":\"ana.rojas\",\"guardianUsername\":\"apoderado.rojas\"," +
                                "\"status\":\"ACTIVE\"}")));
    }

    private void stubAttendanceSummary(long studentId) {
        attendanceWM.stubFor(get(urlEqualTo("/api/attendances/student/" + studentId + "/summary"))
                .willReturn(okJson(String.format(
                        "{\"studentId\":%d,\"present\":8,\"absent\":1,\"justified\":0}", studentId))));
    }

    private void stubGrades(long studentId) {
        gradesWM.stubFor(get(urlEqualTo("/api/grades/student/" + studentId))
                .willReturn(okJson("[{\"id\":1,\"evaluationId\":1,\"subject\":\"Matemática\"," +
                        "\"title\":\"Prueba Unidad 1\",\"studentId\":" + studentId + ",\"score\":6.5}]")));
    }

    // ─────────────────────────────────────────────────────────────────────
    // E2E 1: Resumen académico completo — CORE del negocio
    // BFF orquesta students + attendance + grades en una sola respuesta
    // ─────────────────────────────────────────────────────────────────────
    @Test
    void getAcademicSummary_orchestratesAllThreeServices_returnsConsolidated() {
        stubStudentById(1L);
        stubAttendanceSummary(1L);
        stubGrades(1L);

        given().accept(ContentType.JSON)
            .when().get("/api/bff/students/1/summary")
            .then()
                .statusCode(200)
                .body("student.firstName",         equalTo("Ana"))
                .body("student.status",            equalTo("ACTIVE"))
                .body("attendanceSummary.present", equalTo(8))
                .body("attendanceSummary.absent",  equalTo(1))
                .body("grades",                    hasSize(1))
                .body("grades[0].score",           equalTo(6.5f))
                .body("grades[0].title",           equalTo("Prueba Unidad 1"));

        studentsWM.verify(getRequestedFor(urlEqualTo("/api/students/1")));
        attendanceWM.verify(getRequestedFor(urlEqualTo("/api/attendances/student/1/summary")));
        gradesWM.verify(getRequestedFor(urlEqualTo("/api/grades/student/1")));
    }

    // ─────────────────────────────────────────────────────────────────────
    // E2E 2: Listar estudiantes vía BFF
    // ─────────────────────────────────────────────────────────────────────
    @Test
    void listStudents_proxiesToStudentsService_returnsList() {
        stubStudentsList();

        given().accept(ContentType.JSON)
            .when().get("/api/bff/students")
            .then()
                .statusCode(200)
                .body("$",            hasSize(1))
                .body("[0].lastName", equalTo("Rojas"))
                .body("[0].status",   equalTo("ACTIVE"));

        studentsWM.verify(getRequestedFor(urlEqualTo("/api/students")));
    }

    // ─────────────────────────────────────────────────────────────────────
    // E2E 3: Crear estudiante vía BFF proxy
    // ─────────────────────────────────────────────────────────────────────
    @Test
    void createStudent_proxiesToStudentsService_returns201() {
        stubStudentCreate();

        given()
            .contentType(ContentType.JSON)
            .body("{\"firstName\":\"Ana\",\"lastName\":\"Rojas\",\"course\":\"1° Básico A\"," +
                  "\"email\":\"ana@aulaboh.cl\",\"birthDate\":\"2015-05-10\"}")
            .when().post("/api/students")
            .then()
                .statusCode(201)
                .body("id",     equalTo(1))
                .body("status", equalTo("ACTIVE"));

        studentsWM.verify(postRequestedFor(urlEqualTo("/api/students")));
    }

    // ─────────────────────────────────────────────────────────────────────
    // E2E 4: Estudiante no encontrado → BFF propaga error downstream
    // ─────────────────────────────────────────────────────────────────────
    @Test
    void getAcademicSummary_studentNotFound_propagatesError() {
        studentsWM.stubFor(get(urlEqualTo("/api/students/999"))
                .willReturn(aResponse().withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"StudentNotFoundException\"," +
                                  "\"message\":\"Estudiante 999 no encontrado\"}")));

        given().accept(ContentType.JSON)
            .when().get("/api/bff/students/999/summary")
            .then()
                .statusCode(not(200));
    }

    // ─────────────────────────────────────────────────────────────────────
    // Deshabilita JWT para los tests E2E sin tocar código de producción
    // ─────────────────────────────────────────────────────────────────────
    @Configuration
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }
}
