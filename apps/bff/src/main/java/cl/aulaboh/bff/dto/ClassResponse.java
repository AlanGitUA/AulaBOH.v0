package cl.aulaboh.bff.dto;

import java.time.LocalDate;

public record ClassResponse(Long id, String course, String subject, LocalDate classDate) {}
