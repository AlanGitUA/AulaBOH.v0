package cl.aulaboh.grades.controller;

import cl.aulaboh.grades.dto.*;
import cl.aulaboh.grades.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GradeController {
    private final GradeService service;
    public GradeController(GradeService service) { this.service = service; }

    @PostMapping("/evaluations") @ResponseStatus(HttpStatus.CREATED)
    public EvaluationResponse createEvaluation(@Valid @RequestBody EvaluationRequest request) { return service.createEvaluation(request); }

    @GetMapping("/evaluations")
    public List<EvaluationResponse> evaluations(@RequestParam(required = false) String course) { return service.findEvaluations(course); }

    @PostMapping("/grades") @ResponseStatus(HttpStatus.CREATED)
    public GradeResponse registerGrade(@Valid @RequestBody GradeRequest request) { return service.registerGrade(request); }

    @GetMapping("/grades/student/{studentId}")
    public List<GradeResponse> byStudent(@PathVariable Long studentId) { return service.findByStudent(studentId); }
}
