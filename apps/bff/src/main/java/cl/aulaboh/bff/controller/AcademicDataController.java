package cl.aulaboh.bff.controller;

import cl.aulaboh.bff.client.AttendanceClient;
import cl.aulaboh.bff.client.GradesClient;
import cl.aulaboh.bff.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/bff")
@CrossOrigin(origins = "*")
public class AcademicDataController {
    private final AttendanceClient attendanceClient;
    private final GradesClient gradesClient;

    public AcademicDataController(AttendanceClient attendanceClient, GradesClient gradesClient) {
        this.attendanceClient = attendanceClient;
        this.gradesClient = gradesClient;
    }

    @PostMapping("/classes")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassResponse createClass(@Valid @RequestBody ClassRequest request) {
        return attendanceClient.createClass(request);
    }

    @GetMapping("/classes")
    public List<ClassResponse> classes() {
        ClassResponse[] classes = attendanceClient.findClasses();
        return classes == null ? Collections.emptyList() : Arrays.asList(classes);
    }

    @PostMapping("/attendances")
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceResponse registerAttendance(@Valid @RequestBody AttendanceRequest request) {
        return attendanceClient.registerAttendance(request);
    }

    @GetMapping("/attendances/student/{studentId}")
    public List<AttendanceResponse> attendancesByStudent(@PathVariable Long studentId) {
        AttendanceResponse[] attendances = attendanceClient.attendancesByStudent(studentId);
        return attendances == null ? Collections.emptyList() : Arrays.asList(attendances);
    }

    @PostMapping("/evaluations")
    @ResponseStatus(HttpStatus.CREATED)
    public EvaluationResponse createEvaluation(@Valid @RequestBody EvaluationRequest request) {
        return gradesClient.createEvaluation(request);
    }

    @GetMapping("/evaluations")
    public List<EvaluationResponse> evaluations() {
        EvaluationResponse[] evaluations = gradesClient.findEvaluations();
        return evaluations == null ? Collections.emptyList() : Arrays.asList(evaluations);
    }

    @PostMapping("/grades")
    @ResponseStatus(HttpStatus.CREATED)
    public GradeResponse registerGrade(@Valid @RequestBody GradeRequest request) {
        return gradesClient.registerGrade(request);
    }
}
