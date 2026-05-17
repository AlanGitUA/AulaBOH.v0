package cl.aulaboh.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AttendanceRequest {
    @NotNull(message = "El ID de clase es obligatorio")
    private Long classId;
    @NotNull(message = "El ID de estudiante es obligatorio")
    private Long studentId;
    @NotBlank(message = "El estado de asistencia es obligatorio")
    private String status;
    private String observation;

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
}
