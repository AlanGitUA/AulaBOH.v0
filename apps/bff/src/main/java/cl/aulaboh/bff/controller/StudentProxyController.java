package cl.aulaboh.bff.controller;

import cl.aulaboh.bff.client.StudentClient;
import cl.aulaboh.bff.dto.StudentRequest;
import cl.aulaboh.bff.dto.StudentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentProxyController {
    private final StudentClient studentClient;

    public StudentProxyController(StudentClient studentClient) {
        this.studentClient = studentClient;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentRequest request) {
        return studentClient.create(request);
    }

    @GetMapping
    public List<StudentResponse> findAll() {
        StudentResponse[] students = studentClient.findAll();
        return students == null ? Collections.emptyList() : Arrays.asList(students);
    }

    @GetMapping("/{id}")
    public StudentResponse findById(@PathVariable Long id) {
        return studentClient.findById(id);
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentClient.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentClient.delete(id);
    }
}
