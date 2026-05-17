package cl.aulaboh.students.exception;

public class DuplicateStudentUsernameException extends RuntimeException {
    public DuplicateStudentUsernameException(String username) {
        super("Ya existe un estudiante asociado al usuario " + username);
    }
}
