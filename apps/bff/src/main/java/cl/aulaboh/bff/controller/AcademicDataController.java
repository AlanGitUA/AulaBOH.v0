package cl.aulaboh.bff.controller;

import cl.aulaboh.bff.client.AttendanceClient;
import cl.aulaboh.bff.client.GradesClient;
import cl.aulaboh.bff.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/bff")
@CrossOrigin(origins = "*")
@Tag(name = "Academic Data", description = "Endpoints BFF para clases, asistencia, evaluaciones y calificaciones")
public class AcademicDataController {
    private final AttendanceClient attendanceClient;
    private final GradesClient gradesClient;

    public AcademicDataController(AttendanceClient attendanceClient, GradesClient gradesClient) {
        this.attendanceClient = attendanceClient;
        this.gradesClient = gradesClient;
    }

    @PostMapping("/classes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar clase desde BFF",
            description = "Centraliza el registro de clases para el frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Clase registrada"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public ClassResponse createClass(@Valid @RequestBody ClassRequest request) {
        return attendanceClient.createClass(request);
    }

    @GetMapping("/classes")
    @Operation(
            summary = "Listar clases desde BFF",
            description = "Obtiene clases registradas desde el microservicio de asistencia.",
            responses = @ApiResponse(responseCode = "200", description = "Listado obtenido")
    )
    public List<ClassResponse> classes() {
        ClassResponse[] classes = attendanceClient.findClasses();
        return classes == null ? Collections.emptyList() : Arrays.asList(classes);
    }

    @PostMapping("/attendances")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar asistencia desde BFF",
            description = "Centraliza el registro de asistencia para el frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Asistencia registrada"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public AttendanceResponse registerAttendance(@Valid @RequestBody AttendanceRequest request) {
        return attendanceClient.registerAttendance(request);
    }

    @GetMapping("/attendances/student/{studentId}")
    @Operation(
            summary = "Consultar asistencia desde BFF",
            description = "Obtiene asistencias de un estudiante desde el microservicio de asistencia.",
            responses = @ApiResponse(responseCode = "200", description = "Registros obtenidos")
    )
    public List<AttendanceResponse> attendancesByStudent(@PathVariable Long studentId) {
        AttendanceResponse[] attendances = attendanceClient.attendancesByStudent(studentId);
        return attendances == null ? Collections.emptyList() : Arrays.asList(attendances);
    }

    @PostMapping("/evaluations")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar evaluacion desde BFF",
            description = "Centraliza el registro de evaluaciones para el frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Evaluacion registrada"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public EvaluationResponse createEvaluation(@Valid @RequestBody EvaluationRequest request) {
        return gradesClient.createEvaluation(request);
    }

    @GetMapping("/evaluations")
    @Operation(
            summary = "Listar evaluaciones desde BFF",
            description = "Obtiene evaluaciones desde el microservicio de calificaciones.",
            responses = @ApiResponse(responseCode = "200", description = "Listado obtenido")
    )
    public List<EvaluationResponse> evaluations() {
        EvaluationResponse[] evaluations = gradesClient.findEvaluations();
        return evaluations == null ? Collections.emptyList() : Arrays.asList(evaluations);
    }

    @PostMapping("/grades")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar calificacion desde BFF",
            description = "Centraliza el registro de calificaciones para el frontend.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Calificacion registrada"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public GradeResponse registerGrade(@Valid @RequestBody GradeRequest request) {
        return gradesClient.registerGrade(request);
    }
}
