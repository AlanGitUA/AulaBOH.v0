package cl.aulaboh.attendance.controller;

import cl.aulaboh.attendance.dto.*;
import cl.aulaboh.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AttendanceController {
    private final AttendanceService service;

    public AttendanceController(AttendanceService service) { this.service = service; }

    @PostMapping("/classes")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassResponse createClass(@Valid @RequestBody ClassRequest request) { return service.createClass(request); }

    @GetMapping("/classes")
    public List<ClassResponse> findClasses(@RequestParam(required = false) String course) { return service.findClasses(course); }

    @PostMapping("/attendances")
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceResponse register(@Valid @RequestBody AttendanceRequest request) { return service.registerAttendance(request); }

    @GetMapping("/attendances/student/{studentId}")
    public List<AttendanceResponse> byStudent(@PathVariable Long studentId) { return service.findByStudent(studentId); }

    @GetMapping("/attendances/course/{course}")
    public List<AttendanceResponse> byCourse(@PathVariable String course) { return service.findByCourse(course); }

    @GetMapping("/attendances/student/{studentId}/summary")
    public AttendanceSummaryResponse summary(@PathVariable Long studentId) { return service.getSummary(studentId); }
}
