package cl.aulaboh.bff.dto;

public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        String course,
        String email,
        String studentUsername,
        String guardianUsername,
        String status
) {}
