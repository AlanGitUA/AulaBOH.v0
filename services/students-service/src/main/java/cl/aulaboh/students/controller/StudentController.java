package cl.aulaboh.students.controller;

import cl.aulaboh.students.dto.StudentRequest;
import cl.aulaboh.students.dto.StudentResponse;
import cl.aulaboh.students.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
@Tag(name = "Students", description = "Operacion de administracion de estudiantes")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar estudiante",
            description = "Crea un estudiante activo asociado a un curso.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Estudiante registrado"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
            }
    )
    public StudentResponse create(@Valid @RequestBody StudentRequest request) { return service.create(request); }

    @GetMapping
    @Operation(
            summary = "Listar estudiantes",
            description = "Retorna todos los estudiantes o filtra por curso cuando se envia el parametro course.",
            responses = @ApiResponse(responseCode = "200", description = "Listado obtenido")
    )
    public List<StudentResponse> findAll(@RequestParam(required = false) String course) {
        return course == null ? service.findAll() : service.findByCourse(course);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Consultar estudiante",
            description = "Obtiene el detalle de un estudiante por su identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
                    @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
            }
    )
    public StudentResponse findById(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar estudiante",
            description = "Actualiza los datos basicos de un estudiante existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estudiante actualizado"),
                    @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
                    @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
            }
    )
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar estudiante",
            description = "Elimina un estudiante por su identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Estudiante eliminado"),
                    @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
            }
    )
    public void delete(@PathVariable Long id) { service.delete(id); }
}
