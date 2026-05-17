package cl.aulaboh.attendance.dto;

import java.time.LocalDate;

public record ClassResponse(Long id, String course, String subject, LocalDate classDate) {}
