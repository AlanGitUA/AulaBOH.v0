package cl.aulaboh.grades.repository;
import cl.aulaboh.grades.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> { List<Evaluation> findByCourseIgnoreCase(String course); }
