package cl.aulaboh.attendance.service;

import cl.aulaboh.attendance.client.StudentClient;
import cl.aulaboh.attendance.dto.*;
import cl.aulaboh.attendance.exception.BusinessException;
import cl.aulaboh.attendance.model.*;
import cl.aulaboh.attendance.repository.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AttendanceService {
    private final SchoolClassRepository classRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentClient studentClient;

    public AttendanceService(SchoolClassRepository classRepository, AttendanceRepository attendanceRepository, StudentClient studentClient) {
        this.classRepository = classRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentClient = studentClient;
    }

    @CircuitBreaker(name = "attendanceServiceMethods")
    public ClassResponse createClass(ClassRequest request) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setCourse(request.getCourse());
        schoolClass.setSubject(request.getSubject());
        schoolClass.setClassDate(request.getClassDate());
        return toClassResponse(classRepository.save(schoolClass));
    }

    @CircuitBreaker(name = "attendanceServiceMethods")
    public List<ClassResponse> findClasses(String course) {
        List<SchoolClass> classes = course == null ? classRepository.findAll() : classRepository.findByCourseIgnoreCase(course);
        return classes.stream().map(this::toClassResponse).toList();
    }

    @CircuitBreaker(name = "attendanceServiceMethods")
    public AttendanceResponse registerAttendance(AttendanceRequest request) {
        studentClient.findStudentById(request.getStudentId());
        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new BusinessException("La clase indicada no existe"));
        if (attendanceRepository.existsBySchoolClassIdAndStudentId(request.getClassId(), request.getStudentId())) {
            throw new BusinessException("El estudiante ya tiene asistencia registrada para esta clase");
        }
        Attendance attendance = new Attendance();
        attendance.setSchoolClass(schoolClass);
        attendance.setStudentId(request.getStudentId());
        attendance.setStatus(request.getStatus());
        attendance.setObservation(request.getObservation());
        return toResponse(attendanceRepository.save(attendance));
    }

    @CircuitBreaker(name = "attendanceServiceMethods")
    public List<AttendanceResponse> findByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId).stream().map(this::toResponse).toList();
    }

    @CircuitBreaker(name = "attendanceServiceMethods")
    public List<AttendanceResponse> findByCourse(String course) {
        return attendanceRepository.findBySchoolClassCourseIgnoreCase(course).stream().map(this::toResponse).toList();
    }

    @CircuitBreaker(name = "attendanceServiceMethods")
    public AttendanceSummaryResponse getSummary(Long studentId) {
        return new AttendanceSummaryResponse(
                studentId,
                attendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.PRESENT),
                attendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.ABSENT),
                attendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.JUSTIFIED)
        );
    }

    private ClassResponse toClassResponse(SchoolClass schoolClass) {
        return new ClassResponse(schoolClass.getId(), schoolClass.getCourse(), schoolClass.getSubject(), schoolClass.getClassDate());
    }

    private AttendanceResponse toResponse(Attendance attendance) {
        SchoolClass c = attendance.getSchoolClass();
        return new AttendanceResponse(attendance.getId(), c.getId(), c.getCourse(), c.getSubject(), c.getClassDate(), attendance.getStudentId(), attendance.getStatus(), attendance.getObservation());
    }
}
