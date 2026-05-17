package cl.aulaboh.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class ClassRequest {
    @NotBlank
    @Pattern(
            regexp = "^(?:[1-8]\\u00b0 B\\u00e1sico|[1-4]\\u00b0 Medio) [ABC]$",
            message = "El curso debe ser un nivel valido entre 1° Basico A y 4° Medio C"
    )
    private String course;
    @NotBlank private String subject;
    @NotNull private LocalDate classDate;
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public LocalDate getClassDate() { return classDate; }
    public void setClassDate(LocalDate classDate) { this.classDate = classDate; }
}
