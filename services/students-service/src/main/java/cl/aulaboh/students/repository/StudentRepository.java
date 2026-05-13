package cl.aulaboh.students.repository;

import cl.aulaboh.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Repository Pattern: abstrae la persistencia de estudiantes. */
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCourseIgnoreCase(String course);
}
