package cl.aulaboh.students.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class StudentRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;
    @NotBlank(message = "El curso es obligatorio")
    @Pattern(
            regexp = "^(?:[1-8]\\u00b0 B\\u00e1sico|[1-4]\\u00b0 Medio) [ABC]$",
            message = "El curso debe ser un nivel valido entre 1\u00b0 Basico A y 4\u00b0 Medio C"
    )
    private String course;
    @Email(message = "El correo debe tener formato valido")
    private String email;
    private LocalDate birthDate;
    private String studentUsername;
    private String guardianUsername;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }
    public String getGuardianUsername() { return guardianUsername; }
    public void setGuardianUsername(String guardianUsername) { this.guardianUsername = guardianUsername; }
}
