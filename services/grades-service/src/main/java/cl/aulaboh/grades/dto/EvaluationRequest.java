package cl.aulaboh.grades.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class EvaluationRequest {
    @NotBlank private String course;
    @NotBlank private String subject;
    @NotBlank private String title;
    private LocalDate evaluationDate;
    public String getCourse() { return course; } public void setCourse(String course) { this.course = course; }
    public String getSubject() { return subject; } public void setSubject(String subject) { this.subject = subject; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public LocalDate getEvaluationDate() { return evaluationDate; } public void setEvaluationDate(LocalDate evaluationDate) { this.evaluationDate = evaluationDate; }
}
