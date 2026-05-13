package cl.aulaboh.bff.dto;

public record AttendanceSummaryResponse(Long studentId, Long present, Long absent, Long justified) {}
