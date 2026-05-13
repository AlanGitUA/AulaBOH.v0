package cl.aulaboh.bff.dto;

import java.time.LocalDate;

public record EvaluationResponse(Long id, String course, String subject, String title, LocalDate evaluationDate) {}
