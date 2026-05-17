package cl.aulaboh.bff.controller;

import cl.aulaboh.bff.client.StudentClient;
import cl.aulaboh.bff.config.SecurityConfig;
import cl.aulaboh.bff.dto.StudentResponse;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentProxyController.class)
@Import(SecurityConfig.class)
class StudentProxyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentClient studentClient;

    @MockBean
    private MeterRegistry meterRegistry;

    @Test
    void createsFindsAndListsStudents() throws Exception {
        when(studentClient.create(any()))
                .thenReturn(student(1L));
        when(studentClient.findAll())
                .thenReturn(new StudentResponse[]{student(1L)});
        when(studentClient.findById(1L))
                .thenReturn(student(1L));

        mockMvc.perform(post("/api/students")
                        .with(adminJwt())
                        .contentType("application/json")
                        .content("""
                                {
                                  "firstName":"Ana",
                                  "lastName":"Rojas",
                                  "course":"1° Básico A",
                                  "email":"ana@aulaboh.cl"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/students").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Ana"));

        mockMvc.perform(get("/api/students/1").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course").value("1° Básico A"));
    }

    @Test
    void updatesAndDeletesStudents() throws Exception {
        when(studentClient.update(org.mockito.ArgumentMatchers.eq(1L), any())).thenReturn(student(1L));

        mockMvc.perform(put("/api/students/1")
                        .with(adminJwt())
                        .contentType("application/json")
                        .content("""
                                {
                                  "firstName":"Ana",
                                  "lastName":"Rojas",
                                  "course":"1° Básico A",
                                  "email":"ana@aulaboh.cl"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(delete("/api/students/1").with(adminJwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void returnsEmptyStudentListWhenClientReturnsNull() throws Exception {
        when(studentClient.findAll()).thenReturn(null);

        mockMvc.perform(get("/api/students").with(teacherJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    private StudentResponse student(Long id) {
        return new StudentResponse(id, "Ana", "Rojas", "1° Básico A", "ana@aulaboh.cl",
                "estudiante.demo", "apoderado.demo", "ACTIVE");
    }

    private org.springframework.test.web.servlet.request.RequestPostProcessor adminJwt() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private org.springframework.test.web.servlet.request.RequestPostProcessor teacherJwt() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_DOCENTE"));
    }
}
