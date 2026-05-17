package cl.aulaboh.attendance.repository;

import cl.aulaboh.attendance.model.Attendance;
import cl.aulaboh.attendance.model.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Repository Pattern: abstrae consultas de asistencia. */
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsBySchoolClassIdAndStudentId(Long classId, Long studentId);
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findBySchoolClassCourseIgnoreCase(String course);
    long countByStudentIdAndStatus(Long studentId, AttendanceStatus status);
}
