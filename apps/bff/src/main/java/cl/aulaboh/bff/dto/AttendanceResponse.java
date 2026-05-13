package cl.aulaboh.bff.dto;

import java.time.LocalDate;

public record AttendanceResponse(Long id, Long classId, String course, String subject, LocalDate classDate, Long studentId, String status, String observation) {}
