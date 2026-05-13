package cl.aulaboh.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ClassRequest {
    @NotBlank private String course;
    @NotBlank private String subject;
    @NotNull private LocalDate classDate;
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public LocalDate getClassDate() { return classDate; }
    public void setClassDate(LocalDate classDate) { this.classDate = classDate; }
}
