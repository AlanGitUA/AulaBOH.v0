package cl.aulaboh.attendance.controller;

import cl.aulaboh.attendance.dto.*;
import cl.aulaboh.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Classes and Attendance", description = "Operacion de clases y registros de asistencia")
public class AttendanceController {
    private final AttendanceService service;

    public AttendanceController(AttendanceService service) { this.service = service; }

    @PostMapping("/classes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar clase",
            description = "Crea una clase para un curso y asignatura en una fecha determinada.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Clase registrada"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public ClassResponse createClass(@Valid @RequestBody ClassRequest request) { return service.createClass(request); }

    @GetMapping("/classes")
    @Operation(
            summary = "Listar clases",
            description = "Retorna clases registradas y permite filtrar por curso.",
            responses = @ApiResponse(responseCode = "200", description = "Listado obtenido")
    )
    public List<ClassResponse> findClasses(@RequestParam(required = false) String course) { return service.findClasses(course); }

    @PostMapping("/attendances")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar asistencia",
            description = "Registra la asistencia de un estudiante para una clase existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Asistencia registrada"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public AttendanceResponse register(@Valid @RequestBody AttendanceRequest request) { return service.registerAttendance(request); }

    @GetMapping("/attendances/student/{studentId}")
    @Operation(
            summary = "Consultar asistencia por estudiante",
            description = "Obtiene los registros de asistencia asociados a un estudiante.",
            responses = @ApiResponse(responseCode = "200", description = "Registros obtenidos")
    )
    public List<AttendanceResponse> byStudent(@PathVariable Long studentId) { return service.findByStudent(studentId); }

    @GetMapping("/attendances/course/{course}")
    @Operation(
            summary = "Consultar asistencia por curso",
            description = "Obtiene los registros de asistencia asociados a un curso.",
            responses = @ApiResponse(responseCode = "200", description = "Registros obtenidos")
    )
    public List<AttendanceResponse> byCourse(@PathVariable String course) { return service.findByCourse(course); }

    @GetMapping("/attendances/student/{studentId}/summary")
    @Operation(
            summary = "Resumen de asistencia",
            description = "Calcula totales de asistencia presente, ausente y justificada para un estudiante.",
            responses = @ApiResponse(responseCode = "200", description = "Resumen obtenido")
    )
    public AttendanceSummaryResponse summary(@PathVariable Long studentId) { return service.getSummary(studentId); }
}
