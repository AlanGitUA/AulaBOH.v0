package cl.aulaboh.attendance.repository;

import cl.aulaboh.attendance.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    List<SchoolClass> findByCourseIgnoreCase(String course);
}
