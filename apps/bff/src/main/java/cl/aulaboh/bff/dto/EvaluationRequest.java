package cl.aulaboh.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class EvaluationRequest {
    @NotBlank(message = "El curso es obligatorio")
    @Pattern(
            regexp = "^(?:[1-8]\\u00b0 B\\u00e1sico|[1-4]\\u00b0 Medio) [ABC]$",
            message = "El curso debe ser un nivel valido entre 1° Basico A y 4° Medio C"
    )
    private String course;
    @NotBlank(message = "La asignatura es obligatoria")
    private String subject;
    @NotBlank(message = "El titulo es obligatorio")
    private String title;
    private LocalDate evaluationDate;

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDate evaluationDate) { this.evaluationDate = evaluationDate; }
}
