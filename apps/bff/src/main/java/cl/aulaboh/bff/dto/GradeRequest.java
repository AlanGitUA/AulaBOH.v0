package cl.aulaboh.bff.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class GradeRequest {
    @NotNull(message = "El ID de evaluacion es obligatorio")
    private Long evaluationId;
    @NotNull(message = "El ID de estudiante es obligatorio")
    private Long studentId;
    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "1.0", message = "La nota minima es 1.0")
    @DecimalMax(value = "7.0", message = "La nota maxima es 7.0")
    private Double score;

    public Long getEvaluationId() { return evaluationId; }
    public void setEvaluationId(Long evaluationId) { this.evaluationId = evaluationId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
}
