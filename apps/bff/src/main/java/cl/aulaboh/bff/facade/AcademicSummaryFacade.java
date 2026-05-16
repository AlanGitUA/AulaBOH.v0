package cl.aulaboh.bff.facade;

import cl.aulaboh.bff.client.*;
import cl.aulaboh.bff.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Facade Pattern: entrega una interfaz simple al frontend y orquesta varios microservicios. */
@Component
public class AcademicSummaryFacade {
    private final StudentClient studentClient;
    private final AttendanceClient attendanceClient;
    private final GradesClient gradesClient;

    public AcademicSummaryFacade(StudentClient studentClient, AttendanceClient attendanceClient, GradesClient gradesClient) {
        this.studentClient = studentClient; this.attendanceClient = attendanceClient; this.gradesClient = gradesClient;
    }

    @CircuitBreaker(name = "bffFacade")
    public AcademicSummaryResponse getStudentSummary(Long studentId) {
        StudentResponse student = studentClient.findById(studentId);
        AttendanceSummaryResponse attendance = attendanceClient.summary(studentId);
        GradeResponse[] gradesArray = gradesClient.gradesByStudent(studentId);
        List<GradeResponse> grades = gradesArray == null ? Collections.emptyList() : Arrays.asList(gradesArray);
        return new AcademicSummaryResponse(student, attendance, grades);
    }

    @CircuitBreaker(name = "bffFacade")
    public List<StudentResponse> findStudents() {
        StudentResponse[] students = studentClient.findAll();
        return students == null ? Collections.emptyList() : Arrays.asList(students);
    }

    @CircuitBreaker(name = "bffFacade")
    public StudentResponse createStudent(StudentRequest request) {
        return studentClient.create(request);
    }
}
