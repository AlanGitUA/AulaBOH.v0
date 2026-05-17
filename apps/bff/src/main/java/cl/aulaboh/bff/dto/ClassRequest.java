package cl.aulaboh.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class ClassRequest {
    @NotBlank(message = "La asignatura es obligatoria")
    private String subject;
    @NotBlank(message = "El curso es obligatorio")
    @Pattern(
            regexp = "^(?:[1-8]\\u00b0 B\\u00e1sico|[1-4]\\u00b0 Medio) [ABC]$",
            message = "El curso debe ser un nivel valido entre 1° Basico A y 4° Medio C"
    )
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
