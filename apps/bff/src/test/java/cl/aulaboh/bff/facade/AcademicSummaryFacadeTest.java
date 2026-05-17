package cl.aulaboh.bff.facade;

import cl.aulaboh.bff.client.AttendanceClient;
import cl.aulaboh.bff.client.GradesClient;
import cl.aulaboh.bff.client.StudentClient;
import cl.aulaboh.bff.dto.AcademicSummaryResponse;
import cl.aulaboh.bff.dto.AttendanceSummaryResponse;
import cl.aulaboh.bff.dto.GradeResponse;
import cl.aulaboh.bff.dto.StudentRequest;
import cl.aulaboh.bff.dto.StudentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcademicSummaryFacadeTest {
    @Mock
    private StudentClient studentClient;
    @Mock
    private AttendanceClient attendanceClient;
    @Mock
    private GradesClient gradesClient;

    @InjectMocks
    private AcademicSummaryFacade facade;

    @Test
    void findStudentsReturnsClientStudentsAsList() {
        StudentResponse student = student(1L, "Ana", "Rojas", "1A");
        when(studentClient.findAll()).thenReturn(new StudentResponse[]{student});

        List<StudentResponse> students = facade.findStudents();

        assertThat(students).containsExactly(student);
    }

    @Test
    void findStudentsReturnsEmptyListWhenClientReturnsNull() {
        when(studentClient.findAll()).thenReturn(null);

        List<StudentResponse> students = facade.findStudents();

        assertThat(students).isEmpty();
    }

    @Test
    void createStudentDelegatesToStudentClient() {
        StudentRequest request = studentRequest("Ana", "Rojas", "1A");
        StudentResponse expected = student(1L, "Ana", "Rojas", "1A");
        when(studentClient.create(request)).thenReturn(expected);

        StudentResponse response = facade.createStudent(request);

        assertThat(response).isEqualTo(expected);
        verify(studentClient).create(request);
    }

    @Test
    void getStudentSummaryCombinesStudentAttendanceAndGrades() {
        StudentResponse student = student(1L, "Ana", "Rojas", "1A");
        AttendanceSummaryResponse attendance = new AttendanceSummaryResponse(1L, 8L, 1L, 1L);
        GradeResponse grade = new GradeResponse(10L, 3L, "Matematica", "Prueba 1", 1L, 6.5);
        when(studentClient.findById(1L)).thenReturn(student);
        when(attendanceClient.summary(1L)).thenReturn(attendance);
        when(gradesClient.gradesByStudent(1L)).thenReturn(new GradeResponse[]{grade});

        AcademicSummaryResponse response = facade.getStudentSummary(1L);

        assertThat(response.student()).isEqualTo(student);
        assertThat(response.attendance()).isEqualTo(attendance);
        assertThat(response.grades()).containsExactly(grade);
    }

    @Test
    void getStudentSummaryUsesEmptyGradesWhenClientReturnsNull() {
        StudentResponse student = student(1L, "Ana", "Rojas", "1A");
        AttendanceSummaryResponse attendance = new AttendanceSummaryResponse(1L, 8L, 1L, 1L);
        when(studentClient.findById(1L)).thenReturn(student);
        when(attendanceClient.summary(1L)).thenReturn(attendance);
        when(gradesClient.gradesByStudent(1L)).thenReturn(null);

        AcademicSummaryResponse response = facade.getStudentSummary(1L);

        assertThat(response.grades()).isEmpty();
    }

    @Test
    void getOwnStudentSummaryUsesAuthenticatedStudentMapping() {
        StudentResponse student = student(1L, "Ana", "Rojas", "1A");
        when(studentClient.findByStudentUsername("estudiante.demo")).thenReturn(student);
        when(studentClient.findById(1L)).thenReturn(student);
        when(attendanceClient.summary(1L)).thenReturn(new AttendanceSummaryResponse(1L, 0L, 0L, 0L));
        when(gradesClient.gradesByStudent(1L)).thenReturn(new GradeResponse[0]);

        AcademicSummaryResponse response = facade.getOwnStudentSummary("estudiante.demo");

        assertThat(response.student()).isEqualTo(student);
    }

    @Test
    void findGuardianStudentsReturnsOnlyAssignedStudents() {
        StudentResponse student = student(1L, "Ana", "Rojas", "1A");
        when(studentClient.findByGuardianUsername("apoderado.demo")).thenReturn(new StudentResponse[]{student});

        List<StudentResponse> students = facade.findGuardianStudents("apoderado.demo");

        assertThat(students).containsExactly(student);
    }

    private StudentRequest studentRequest(String firstName, String lastName, String course) {
        StudentRequest request = new StudentRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setCourse(course);
        request.setEmail(firstName.toLowerCase() + "@aulaboh.cl");
        request.setBirthDate(LocalDate.of(2010, 5, 13));
        return request;
    }

    private StudentResponse student(Long id, String firstName, String lastName, String course) {
        return new StudentResponse(
                id,
                firstName,
                lastName,
                course,
                firstName.toLowerCase() + "@aulaboh.cl",
                "estudiante.demo",
                "apoderado.demo",
                "ACTIVE"
        );
    }
}
