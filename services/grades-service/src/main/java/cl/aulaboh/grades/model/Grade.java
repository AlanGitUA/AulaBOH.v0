package cl.aulaboh.grades.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) private Evaluation evaluation;
    @Column(nullable = false) private Long studentId;
    @Column(nullable = false) private Double score;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Evaluation getEvaluation() { return evaluation; } public void setEvaluation(Evaluation evaluation) { this.evaluation = evaluation; }
    public Long getStudentId() { return studentId; } public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Double getScore() { return score; } public void setScore(Double score) { this.score = score; }
}
