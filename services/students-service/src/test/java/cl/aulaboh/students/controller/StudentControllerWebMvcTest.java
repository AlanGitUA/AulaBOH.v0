package cl.aulaboh.students.controller;

import cl.aulaboh.students.dto.StudentResponse;
import cl.aulaboh.students.service.StudentService;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @MockBean
    private MeterRegistry meterRegistry;

    @Test
    void createReturnsCreatedStudent() throws Exception {
        when(service.create(org.mockito.ArgumentMatchers.any()))
                .thenReturn(student(1L, "1\u00b0 B\u00e1sico A"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Ana",
                                  "lastName": "Perez",
                                  "course": "1\u00b0 B\u00e1sico A",
                                  "email": "ana@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.course").value("1\u00b0 B\u00e1sico A"));
    }

    @Test
    void createRejectsInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "",
                                  "lastName": "Perez",
                                  "course": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationException"));
    }

    @Test
    void createRejectsCourseOutsideCatalog() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Ana",
                                  "lastName": "Perez",
                                  "course": "9\u00b0 B\u00e1sico A"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationException"));
    }

    @Test
    void findAllUsesCourseFilterWhenProvided() throws Exception {
        when(service.findByCourse("1\u00b0 B\u00e1sico A")).thenReturn(List.of(student(1L, "1\u00b0 B\u00e1sico A")));

        mockMvc.perform(get("/api/students").param("course", "1\u00b0 B\u00e1sico A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].course").value("1\u00b0 B\u00e1sico A"));

        verify(service).findByCourse("1\u00b0 B\u00e1sico A");
    }

    @Test
    void exposesUsernameAndGuardianLookups() throws Exception {
        when(service.findByStudentUsername("ana.perez")).thenReturn(student(1L, "1\u00b0 B\u00e1sico A"));
        when(service.findByGuardianUsername("maria.perez")).thenReturn(List.of(student(1L, "1\u00b0 B\u00e1sico A")));

        mockMvc.perform(get("/api/students/username/ana.perez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        mockMvc.perform(get("/api/students/guardian/maria.perez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void updateAndDeleteDelegateToService() throws Exception {
        when(service.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
                .thenReturn(student(1L, "2\u00b0 Medio B"));

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Ana",
                                  "lastName": "Perez",
                                  "course": "2\u00b0 Medio B",
                                  "email": "ana@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course").value("2\u00b0 Medio B"));

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    private StudentResponse student(Long id, String course) {
        return new StudentResponse(id, "Ana", "Perez", course, "ana@example.com", "ana.perez", "maria.perez", "ACTIVE");
    }
}
