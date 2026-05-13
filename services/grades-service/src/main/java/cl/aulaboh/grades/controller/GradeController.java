package cl.aulaboh.grades.controller;

import cl.aulaboh.grades.dto.*;
import cl.aulaboh.grades.service.GradeService;
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
@Tag(name = "Evaluations and Grades", description = "Operacion de evaluaciones y calificaciones")
public class GradeController {
    private final GradeService service;
    public GradeController(GradeService service) { this.service = service; }

    @PostMapping("/evaluations") @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar evaluacion",
            description = "Crea una evaluacion para un curso y asignatura.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Evaluacion registrada"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public EvaluationResponse createEvaluation(@Valid @RequestBody EvaluationRequest request) { return service.createEvaluation(request); }

    @GetMapping("/evaluations")
    @Operation(
            summary = "Listar evaluaciones",
            description = "Retorna evaluaciones registradas y permite filtrar por curso.",
            responses = @ApiResponse(responseCode = "200", description = "Listado obtenido")
    )
    public List<EvaluationResponse> evaluations(@RequestParam(required = false) String course) { return service.findEvaluations(course); }

    @PostMapping("/grades") @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar calificacion",
            description = "Registra la calificacion de un estudiante en una evaluacion existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Calificacion registrada"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public GradeResponse registerGrade(@Valid @RequestBody GradeRequest request) { return service.registerGrade(request); }

    @GetMapping("/grades/student/{studentId}")
    @Operation(
            summary = "Consultar calificaciones por estudiante",
            description = "Obtiene las calificaciones asociadas a un estudiante.",
            responses = @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas")
    )
    public List<GradeResponse> byStudent(@PathVariable Long studentId) { return service.findByStudent(studentId); }
}
