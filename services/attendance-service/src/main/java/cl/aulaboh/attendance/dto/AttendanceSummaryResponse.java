package cl.aulaboh.attendance.dto;

public record AttendanceSummaryResponse(Long studentId, long present, long absent, long justified) {}
