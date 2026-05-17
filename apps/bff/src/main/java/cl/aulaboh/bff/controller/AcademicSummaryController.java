package cl.aulaboh.bff.controller;

import cl.aulaboh.bff.dto.*;
import cl.aulaboh.bff.facade.AcademicSummaryFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bff")
@CrossOrigin(origins = "*")
@Tag(name = "Academic Summary", description = "Endpoints agregados para consumo del frontend")
public class AcademicSummaryController {
    private final AcademicSummaryFacade facade;
    public AcademicSummaryController(AcademicSummaryFacade facade) { this.facade = facade; }

    @GetMapping("/students")
    @Operation(
            summary = "Listar estudiantes desde BFF",
            description = "Obtiene estudiantes a traves de la capa BFF.",
            responses = @ApiResponse(responseCode = "200", description = "Listado obtenido")
    )
    public List<StudentResponse> students() { return facade.findStudents(); }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar estudiante desde BFF",
            description = "Centraliza el registro de estudiantes para el frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Estudiante registrado"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public StudentResponse createStudent(@Valid @RequestBody StudentRequest request) { return facade.createStudent(request); }

    @GetMapping("/students/{studentId}/summary")
    @Operation(
            summary = "Obtener resumen academico",
            description = "Combina datos de estudiantes, asistencia y calificaciones en una respuesta orientada al frontend.",
            responses = @ApiResponse(responseCode = "200", description = "Resumen obtenido")
    )
    public AcademicSummaryResponse studentSummary(@PathVariable Long studentId) { return facade.getStudentSummary(studentId); }

    @GetMapping("/me/summary")
    public AcademicSummaryResponse ownStudentSummary(@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) {
        return facade.getOwnStudentSummary(jwt.getClaimAsString("preferred_username"));
    }

    @GetMapping("/me/students")
    public List<StudentResponse> guardianStudents(@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) {
        return facade.findGuardianStudents(jwt.getClaimAsString("preferred_username"));
    }

    @GetMapping("/me/students/{studentId}/summary")
    public AcademicSummaryResponse guardianStudentSummary(
            @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt,
            @PathVariable Long studentId
    ) {
        return facade.getGuardianStudentSummary(jwt.getClaimAsString("preferred_username"), studentId);
    }
}
