package cl.aulaboh.attendance.dto;

import cl.aulaboh.attendance.model.AttendanceStatus;
import java.time.LocalDate;

public record AttendanceResponse(Long id, Long classId, String course, String subject, LocalDate classDate, Long studentId, AttendanceStatus status, String observation) {}
