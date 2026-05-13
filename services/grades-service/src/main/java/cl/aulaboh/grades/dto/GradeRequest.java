package cl.aulaboh.grades.dto;

import jakarta.validation.constraints.NotNull;

public class GradeRequest {
    @NotNull private Long evaluationId;
    @NotNull private Long studentId;
    @NotNull private Double score;
    public Long getEvaluationId() { return evaluationId; } public void setEvaluationId(Long evaluationId) { this.evaluationId = evaluationId; }
    public Long getStudentId() { return studentId; } public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Double getScore() { return score; } public void setScore(Double score) { this.score = score; }
}
