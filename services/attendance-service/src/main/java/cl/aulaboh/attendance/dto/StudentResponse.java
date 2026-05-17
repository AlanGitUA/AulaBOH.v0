package cl.aulaboh.attendance.dto;

public record StudentResponse(Long id, String firstName, String lastName, String course, String email, String status) {}
