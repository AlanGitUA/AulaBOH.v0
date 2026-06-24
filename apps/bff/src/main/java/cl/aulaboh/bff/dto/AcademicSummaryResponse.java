package cl.aulaboh.bff.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AcademicSummaryResponse(
        StudentResponse student,
        @JsonProperty("attendanceSummary") AttendanceSummaryResponse attendance,
        List<GradeResponse> grades
) {}
