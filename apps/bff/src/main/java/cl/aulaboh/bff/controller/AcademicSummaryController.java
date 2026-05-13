package cl.aulaboh.bff.controller;

import cl.aulaboh.bff.dto.*;
import cl.aulaboh.bff.facade.AcademicSummaryFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bff")
@CrossOrigin(origins = "*")
public class AcademicSummaryController {
    private final AcademicSummaryFacade facade;
    public AcademicSummaryController(AcademicSummaryFacade facade) { this.facade = facade; }

    @GetMapping("/students")
    public List<StudentResponse> students() { return facade.findStudents(); }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(@Valid @RequestBody StudentRequest request) { return facade.createStudent(request); }

    @GetMapping("/students/{studentId}/summary")
    public AcademicSummaryResponse studentSummary(@PathVariable Long studentId) { return facade.getStudentSummary(studentId); }
}
