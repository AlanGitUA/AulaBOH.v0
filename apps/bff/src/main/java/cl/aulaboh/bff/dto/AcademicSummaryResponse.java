package cl.aulaboh.bff.dto;

import java.util.List;

public record AcademicSummaryResponse(StudentResponse student, AttendanceSummaryResponse attendance, List<GradeResponse> grades) {}
