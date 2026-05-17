package cl.aulaboh.grades.service;

import cl.aulaboh.grades.client.StudentClient;
import cl.aulaboh.grades.dto.*;
import cl.aulaboh.grades.exception.BusinessException;
import cl.aulaboh.grades.exception.DuplicateEvaluationException;
import cl.aulaboh.grades.model.*;
import cl.aulaboh.grades.repository.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GradeService {
    private final EvaluationRepository evaluationRepository;
    private final GradeRepository gradeRepository;
    private final StudentClient studentClient;

    public GradeService(EvaluationRepository evaluationRepository, GradeRepository gradeRepository, StudentClient studentClient) {
        this.evaluationRepository = evaluationRepository; this.gradeRepository = gradeRepository; this.studentClient = studentClient;
    }

    @CircuitBreaker(name = "gradesServiceMethods")
    public EvaluationResponse createEvaluation(EvaluationRequest request) {
        validateUniqueEvaluation(request);
        Evaluation e = new Evaluation();
        e.setCourse(request.getCourse());
        e.setSubject(request.getSubject());
        e.setTitle(request.getTitle());
        e.setEvaluationDate(request.getEvaluationDate());
        return toEvaluationResponse(evaluationRepository.save(e));
    }

    @CircuitBreaker(name = "gradesServiceMethods")
    public List<EvaluationResponse> findEvaluations(String course) {
        List<Evaluation> evaluations = course == null ? evaluationRepository.findAll() : evaluationRepository.findByCourseIgnoreCase(course);
        return evaluations.stream().map(this::toEvaluationResponse).toList();
    }

    @CircuitBreaker(name = "gradesServiceMethods")
    public EvaluationResponse updateEvaluation(Long id, EvaluationRequest request) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("La evaluacion no existe"));
        validateUniqueEvaluation(request, id);
        evaluation.setCourse(request.getCourse());
        evaluation.setSubject(request.getSubject());
        evaluation.setTitle(request.getTitle());
        evaluation.setEvaluationDate(request.getEvaluationDate());
        return toEvaluationResponse(evaluationRepository.save(evaluation));
    }

    @CircuitBreaker(name = "gradesServiceMethods")
    public GradeResponse registerGrade(GradeRequest request) {
        if (request.getScore() < 1.0 || request.getScore() > 7.0) throw new BusinessException("La nota debe estar entre 1.0 y 7.0");
        studentClient.findStudentById(request.getStudentId());
        Evaluation evaluation = evaluationRepository.findById(request.getEvaluationId()).orElseThrow(() -> new BusinessException("La evaluacion no existe"));
        Grade grade = new Grade(); grade.setEvaluation(evaluation); grade.setStudentId(request.getStudentId()); grade.setScore(request.getScore());
        return toResponse(gradeRepository.save(grade));
    }

    @CircuitBreaker(name = "gradesServiceMethods")
    public List<GradeResponse> findByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream().map(this::toResponse).toList();
    }

    private EvaluationResponse toEvaluationResponse(Evaluation evaluation) {
        return new EvaluationResponse(evaluation.getId(), evaluation.getCourse(), evaluation.getSubject(), evaluation.getTitle(), evaluation.getEvaluationDate());
    }

    private void validateUniqueEvaluation(EvaluationRequest request) {
        boolean duplicate = evaluationRepository
                .existsByCourseIgnoreCaseAndSubjectIgnoreCaseAndTitleIgnoreCaseAndEvaluationDate(
                        request.getCourse(),
                        request.getSubject(),
                        request.getTitle(),
                        request.getEvaluationDate()
                );
        if (duplicate) {
            throw new DuplicateEvaluationException();
        }
    }

    private void validateUniqueEvaluation(EvaluationRequest request, Long currentEvaluationId) {
        boolean duplicate = evaluationRepository
                .existsByCourseIgnoreCaseAndSubjectIgnoreCaseAndTitleIgnoreCaseAndEvaluationDateAndIdNot(
                        request.getCourse(),
                        request.getSubject(),
                        request.getTitle(),
                        request.getEvaluationDate(),
                        currentEvaluationId
                );
        if (duplicate) {
            throw new DuplicateEvaluationException();
        }
    }

    private GradeResponse toResponse(Grade grade) {
        Evaluation e = grade.getEvaluation();
        return new GradeResponse(grade.getId(), e.getId(), e.getSubject(), e.getTitle(), grade.getStudentId(), grade.getScore());
    }
}
