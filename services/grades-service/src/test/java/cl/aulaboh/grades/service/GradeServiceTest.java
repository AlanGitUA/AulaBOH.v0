package cl.aulaboh.grades.service;

import cl.aulaboh.grades.client.StudentClient;
import cl.aulaboh.grades.dto.EvaluationRequest;
import cl.aulaboh.grades.dto.GradeRequest;
import cl.aulaboh.grades.dto.GradeResponse;
import cl.aulaboh.grades.exception.BusinessException;
import cl.aulaboh.grades.model.Evaluation;
import cl.aulaboh.grades.model.Grade;
import cl.aulaboh.grades.repository.EvaluationRepository;
import cl.aulaboh.grades.repository.GradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {
    @Mock
    private EvaluationRepository evaluationRepository;
    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private StudentClient studentClient;

    @InjectMocks
    private GradeService service;

    @Test
    void createEvaluationPersistsEvaluationData() {
        Evaluation saved = evaluation(1L, "1A", "Matematica", "Prueba 1");
        when(evaluationRepository.save(org.mockito.ArgumentMatchers.any(Evaluation.class))).thenReturn(saved);

        var response = service.createEvaluation(evaluationRequest("1A", "Matematica", "Prueba 1"));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Prueba 1");
    }

    @Test
    void findEvaluationsFiltersByCourseWhenProvided() {
        when(evaluationRepository.findByCourseIgnoreCase("1A"))
                .thenReturn(List.of(evaluation(1L, "1A", "Matematica", "Prueba 1")));

        var responses = service.findEvaluations("1A");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).course()).isEqualTo("1A");
    }

    @Test
    void registerGradePersistsValidScore() {
        Evaluation evaluation = evaluation(1L, "1A", "Matematica", "Prueba 1");
        Grade saved = grade(10L, evaluation, 5L, 6.5);
        when(evaluationRepository.findById(1L)).thenReturn(Optional.of(evaluation));
        when(gradeRepository.save(org.mockito.ArgumentMatchers.any(Grade.class))).thenReturn(saved);

        GradeResponse response = service.registerGrade(gradeRequest(1L, 5L, 6.5));

        verify(studentClient).findStudentById(5L);
        assertThat(response.score()).isEqualTo(6.5);
        assertThat(response.evaluationId()).isEqualTo(1L);
    }

    @Test
    void registerGradeRejectsScoreBelowRange() {
        assertThatThrownBy(() -> service.registerGrade(gradeRequest(1L, 5L, 0.9)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("nota");
    }

    @Test
    void registerGradeRejectsScoreAboveRange() {
        assertThatThrownBy(() -> service.registerGrade(gradeRequest(1L, 5L, 7.1)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("nota");
    }

    @Test
    void registerGradeRejectsMissingEvaluation() {
        when(evaluationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registerGrade(gradeRequest(1L, 5L, 6.0)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("evaluacion");
    }

    @Test
    void findByStudentReturnsGrades() {
        Evaluation evaluation = evaluation(1L, "1A", "Matematica", "Prueba 1");
        when(gradeRepository.findByStudentId(5L)).thenReturn(List.of(grade(10L, evaluation, 5L, 6.5)));

        List<GradeResponse> responses = service.findByStudent(5L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).studentId()).isEqualTo(5L);
    }

    private EvaluationRequest evaluationRequest(String course, String subject, String title) {
        EvaluationRequest request = new EvaluationRequest();
        request.setCourse(course);
        request.setSubject(subject);
        request.setTitle(title);
        request.setEvaluationDate(LocalDate.of(2026, 5, 13));
        return request;
    }

    private GradeRequest gradeRequest(Long evaluationId, Long studentId, Double score) {
        GradeRequest request = new GradeRequest();
        request.setEvaluationId(evaluationId);
        request.setStudentId(studentId);
        request.setScore(score);
        return request;
    }

    private Evaluation evaluation(Long id, String course, String subject, String title) {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(id);
        evaluation.setCourse(course);
        evaluation.setSubject(subject);
        evaluation.setTitle(title);
        evaluation.setEvaluationDate(LocalDate.of(2026, 5, 13));
        return evaluation;
    }

    private Grade grade(Long id, Evaluation evaluation, Long studentId, Double score) {
        Grade grade = new Grade();
        grade.setId(id);
        grade.setEvaluation(evaluation);
        grade.setStudentId(studentId);
        grade.setScore(score);
        return grade;
    }
}
