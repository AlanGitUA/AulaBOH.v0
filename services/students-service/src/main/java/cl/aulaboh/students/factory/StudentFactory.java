package cl.aulaboh.students.factory;

import cl.aulaboh.students.dto.StudentRequest;
import cl.aulaboh.students.model.Student;

/**
 * Factory Method: centraliza la creacion de estudiantes y aplica valores por defecto.
 */
public final class StudentFactory {
    private StudentFactory() {}

    public static Student createActiveStudent(StudentRequest request) {
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setCourse(request.getCourse());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setStudentUsername(request.getStudentUsername());
        student.setGuardianUsername(request.getGuardianUsername());
        student.setStatus("ACTIVE");
        return student;
    }
}
