package cl.aulaboh.students.service;

import cl.aulaboh.students.dto.StudentRequest;
import cl.aulaboh.students.dto.StudentResponse;
import cl.aulaboh.students.exception.DuplicateStudentUsernameException;
import cl.aulaboh.students.exception.StudentNotFoundException;
import cl.aulaboh.students.factory.StudentFactory;
import cl.aulaboh.students.model.Student;
import cl.aulaboh.students.repository.StudentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @CircuitBreaker(name = "studentsServiceMethods")
    public StudentResponse create(StudentRequest request) {
        validateUniqueStudentUsername(request.getStudentUsername());
        Student saved = repository.save(StudentFactory.createActiveStudent(request));
        return toResponse(saved);
    }

    @CircuitBreaker(name = "studentsServiceMethods")
    public List<StudentResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @CircuitBreaker(name = "studentsServiceMethods")
    public StudentResponse findById(Long id) {
        return repository.findById(id).map(this::toResponse).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @CircuitBreaker(name = "studentsServiceMethods")
    public List<StudentResponse> findByCourse(String course) {
        return repository.findByCourseIgnoreCase(course).stream().map(this::toResponse).toList();
    }

    @CircuitBreaker(name = "studentsServiceMethods")
    public StudentResponse findByStudentUsername(String username) {
        return repository.findByStudentUsernameIgnoreCase(username)
                .map(this::toResponse)
                .orElseThrow(() -> new StudentNotFoundException(username));
    }

    @CircuitBreaker(name = "studentsServiceMethods")
    public List<StudentResponse> findByGuardianUsername(String username) {
        return repository.findByGuardianUsernameIgnoreCase(username).stream().map(this::toResponse).toList();
    }

    @CircuitBreaker(name = "studentsServiceMethods")
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        validateUniqueStudentUsername(request.getStudentUsername(), id);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setCourse(request.getCourse());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setStudentUsername(request.getStudentUsername());
        student.setGuardianUsername(request.getGuardianUsername());
        return toResponse(repository.save(student));
    }

    @CircuitBreaker(name = "studentsServiceMethods")
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new StudentNotFoundException(id);
        repository.deleteById(id);
    }

    private void validateUniqueStudentUsername(String username) {
        if (hasText(username) && repository.existsByStudentUsernameIgnoreCase(username)) {
            throw new DuplicateStudentUsernameException(username);
        }
    }

    private void validateUniqueStudentUsername(String username, Long currentStudentId) {
        if (hasText(username) && repository.existsByStudentUsernameIgnoreCaseAndIdNot(username, currentStudentId)) {
            throw new DuplicateStudentUsernameException(username);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getCourse(),
                student.getEmail(),
                student.getStudentUsername(),
                student.getGuardianUsername(),
                student.getStatus()
        );
    }
}
