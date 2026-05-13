package cl.aulaboh.grades.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String course;
    @Column(nullable = false) private String subject;
    @Column(nullable = false) private String title;
    private LocalDate evaluationDate;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getCourse() { return course; } public void setCourse(String course) { this.course = course; }
    public String getSubject() { return subject; } public void setSubject(String subject) { this.subject = subject; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public LocalDate getEvaluationDate() { return evaluationDate; } public void setEvaluationDate(LocalDate evaluationDate) { this.evaluationDate = evaluationDate; }
}
