package cl.aulaboh.grades.dto;

public record GradeResponse(Long id, Long evaluationId, String subject, String title, Long studentId, Double score) {}
