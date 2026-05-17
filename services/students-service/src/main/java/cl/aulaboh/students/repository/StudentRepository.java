package cl.aulaboh.students.repository;

import cl.aulaboh.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/** Repository Pattern: abstrae la persistencia de estudiantes. */
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByStatusIgnoreCase(String status);
    List<Student> findByCourseIgnoreCaseAndStatusIgnoreCase(String course, String status);
    Optional<Student> findByStudentUsernameIgnoreCaseAndStatusIgnoreCase(String studentUsername, String status);
    List<Student> findByGuardianUsernameIgnoreCaseAndStatusIgnoreCase(String guardianUsername, String status);
    boolean existsByStudentUsernameIgnoreCase(String studentUsername);
    boolean existsByStudentUsernameIgnoreCaseAndIdNot(String studentUsername, Long id);
}
