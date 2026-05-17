package cl.aulaboh.bff.controller;

import cl.aulaboh.bff.client.AttendanceClient;
import cl.aulaboh.bff.client.GradesClient;
import cl.aulaboh.bff.config.SecurityConfig;
import cl.aulaboh.bff.dto.AttendanceSummaryResponse;
import cl.aulaboh.bff.dto.StudentResponse;
import cl.aulaboh.bff.facade.AcademicSummaryFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AcademicSummaryController.class, AcademicDataController.class})
@Import(SecurityConfig.class)
class BffSecurityWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcademicSummaryFacade academicSummaryFacade;

    @MockBean
    private AttendanceClient attendanceClient;

    @MockBean
    private GradesClient gradesClient;

    @Test
    void returnsUnauthorizedWithoutToken() throws Exception {
        mockMvc.perform(get("/api/bff/students"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsStudentRoleFromGeneralStudentList() throws Exception {
        mockMvc.perform(get("/api/bff/students")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void allowsTeacherToReadGeneralStudentList() throws Exception {
        when(academicSummaryFacade.findStudents()).thenReturn(List.of());

        mockMvc.perform(get("/api/bff/students")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_DOCENTE"))))
                .andExpect(status().isOk());
    }

    @Test
    void rejectsTeacherFromCreatingStudents() throws Exception {
        mockMvc.perform(post("/api/bff/students")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_DOCENTE")))
                        .contentType("application/json")
                        .content("""
                                {
                                  "firstName":"Ana",
                                  "lastName":"Rojas",
                                  "course":"1A"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void allowsStudentToReadOwnSummary() throws Exception {
        when(academicSummaryFacade.getOwnStudentSummary("estudiante.demo"))
                .thenReturn(new cl.aulaboh.bff.dto.AcademicSummaryResponse(
                        student(1L),
                        new AttendanceSummaryResponse(1L, 0L, 0L, 0L),
                        List.of()
                ));

        mockMvc.perform(get("/api/bff/me/summary")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "estudiante.demo"))
                                .authorities(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"))))
                .andExpect(status().isOk());
    }

    @Test
    void allowsGuardianToReadAssignedStudentSummary() throws Exception {
        when(academicSummaryFacade.getGuardianStudentSummary("apoderado.demo", 1L))
                .thenReturn(new cl.aulaboh.bff.dto.AcademicSummaryResponse(
                        student(1L),
                        new AttendanceSummaryResponse(1L, 0L, 0L, 0L),
                        List.of()
                ));

        mockMvc.perform(get("/api/bff/me/students/1/summary")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "apoderado.demo"))
                                .authorities(new SimpleGrantedAuthority("ROLE_APODERADO"))))
                .andExpect(status().isOk());
    }

    private StudentResponse student(Long id) {
        return new StudentResponse(id, "Ana", "Rojas", "1A", "ana@aulaboh.cl", "estudiante.demo", "apoderado.demo", "ACTIVE");
    }
}
