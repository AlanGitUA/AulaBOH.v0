package cl.aulaboh.attendance.dto;

import cl.aulaboh.attendance.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public class AttendanceRequest {
    @NotNull private Long classId;
    @NotNull private Long studentId;
    @NotNull private AttendanceStatus status;
    private String observation;
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
}
