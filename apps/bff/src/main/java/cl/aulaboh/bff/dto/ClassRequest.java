package cl.aulaboh.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ClassRequest {
    @NotBlank(message = "La asignatura es obligatoria")
    private String subject;
    @NotBlank(message = "El curso es obligatorio")
    private String course;
    @NotNull(message = "La fecha de clase es obligatoria")
    private LocalDate classDate;

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public LocalDate getClassDate() { return classDate; }
    public void setClassDate(LocalDate classDate) { this.classDate = classDate; }
}
