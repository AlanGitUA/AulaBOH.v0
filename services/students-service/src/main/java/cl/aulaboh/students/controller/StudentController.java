package cl.aulaboh.students.controller;

import cl.aulaboh.students.dto.StudentRequest;
import cl.aulaboh.students.dto.StudentResponse;
import cl.aulaboh.students.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentRequest request) { return service.create(request); }

    @GetMapping
    public List<StudentResponse> findAll(@RequestParam(required = false) String course) {
        return course == null ? service.findAll() : service.findByCourse(course);
    }

    @GetMapping("/{id}")
    public StudentResponse findById(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
