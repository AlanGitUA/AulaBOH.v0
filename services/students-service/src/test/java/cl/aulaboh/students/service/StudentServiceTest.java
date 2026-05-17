package cl.aulaboh.students.service;

import cl.aulaboh.students.dto.StudentRequest;
import cl.aulaboh.students.dto.StudentResponse;
import cl.aulaboh.students.exception.StudentNotFoundException;
import cl.aulaboh.students.model.Student;
import cl.aulaboh.students.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    @Test
    void createPersistsActiveStudent() {
        Student saved = student(1L, "Ana", "Rojas", "1A", "ana@aulaboh.cl");
        when(repository.save(org.mockito.ArgumentMatchers.any(Student.class))).thenReturn(saved);

        StudentResponse response = service.create(request("Ana", "Rojas", "1A", "ana@aulaboh.cl"));

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("ACTIVE");
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.course()).isEqualTo("1A");
    }

    @Test
    void findByIdReturnsStudentWhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(student(1L, "Ana", "Rojas", "1A", "ana@aulaboh.cl")));

        StudentResponse response = service.findById(1L);

        assertThat(response.firstName()).isEqualTo("Ana");
        assertThat(response.email()).isEqualTo("ana@aulaboh.cl");
    }

    @Test
    void findByIdThrowsWhenStudentDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void updateChangesStudentFields() {
        Student existing = student(1L, "Ana", "Rojas", "1A", "ana@aulaboh.cl");
        Student updated = student(1L, "Ana Maria", "Rojas", "2B", "anamaria@aulaboh.cl");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(updated);

        StudentResponse response = service.update(1L, request("Ana Maria", "Rojas", "2B", "anamaria@aulaboh.cl"));

        assertThat(existing.getFirstName()).isEqualTo("Ana Maria");
        assertThat(existing.getCourse()).isEqualTo("2B");
        assertThat(response.email()).isEqualTo("anamaria@aulaboh.cl");
    }

    @Test
    void findByCourseReturnsMatchingStudents() {
        when(repository.findByCourseIgnoreCase("1A")).thenReturn(List.of(student(1L, "Ana", "Rojas", "1A", null)));

        List<StudentResponse> responses = service.findByCourse("1A");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).course()).isEqualTo("1A");
    }

    @Test
    void findByStudentUsernameReturnsAssociatedStudent() {
        when(repository.findByStudentUsernameIgnoreCase("estudiante.demo"))
                .thenReturn(Optional.of(student(1L, "Ana", "Rojas", "1A", "ana@aulaboh.cl")));

        StudentResponse response = service.findByStudentUsername("estudiante.demo");

        assertThat(response.studentUsername()).isEqualTo("estudiante.demo");
    }

    @Test
    void findByGuardianUsernameReturnsRepresentedStudents() {
        when(repository.findByGuardianUsernameIgnoreCase("apoderado.demo"))
                .thenReturn(List.of(student(1L, "Ana", "Rojas", "1A", "ana@aulaboh.cl")));

        List<StudentResponse> responses = service.findByGuardianUsername("apoderado.demo");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).guardianUsername()).isEqualTo("apoderado.demo");
    }

    @Test
    void deleteThrowsWhenStudentDoesNotExist() {
        when(repository.existsById(7L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(7L))
                .isInstanceOf(StudentNotFoundException.class);
    }

    private StudentRequest request(String firstName, String lastName, String course, String email) {
        StudentRequest request = new StudentRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setCourse(course);
        request.setEmail(email);
        request.setBirthDate(LocalDate.of(2010, 5, 13));
        return request;
    }

    private Student student(Long id, String firstName, String lastName, String course, String email) {
        Student student = new Student();
        student.setId(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setCourse(course);
        student.setEmail(email);
        student.setBirthDate(LocalDate.of(2010, 5, 13));
        student.setStudentUsername("estudiante.demo");
        student.setGuardianUsername("apoderado.demo");
        student.setStatus("ACTIVE");
        return student;
    }
}
