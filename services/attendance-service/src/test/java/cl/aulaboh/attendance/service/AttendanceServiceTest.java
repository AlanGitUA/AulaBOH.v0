package cl.aulaboh.attendance.service;

import cl.aulaboh.attendance.client.StudentClient;
import cl.aulaboh.attendance.dto.AttendanceRequest;
import cl.aulaboh.attendance.dto.AttendanceResponse;
import cl.aulaboh.attendance.dto.AttendanceSummaryResponse;
import cl.aulaboh.attendance.dto.ClassRequest;
import cl.aulaboh.attendance.exception.BusinessException;
import cl.aulaboh.attendance.model.Attendance;
import cl.aulaboh.attendance.model.AttendanceStatus;
import cl.aulaboh.attendance.model.SchoolClass;
import cl.aulaboh.attendance.repository.AttendanceRepository;
import cl.aulaboh.attendance.repository.SchoolClassRepository;
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
class AttendanceServiceTest {
    @Mock
    private SchoolClassRepository classRepository;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private StudentClient studentClient;

    @InjectMocks
    private AttendanceService service;

    @Test
    void createClassPersistsClassData() {
        SchoolClass saved = schoolClass(1L, "1A", "Matematica");
        when(classRepository.save(org.mockito.ArgumentMatchers.any(SchoolClass.class))).thenReturn(saved);

        var response = service.createClass(classRequest("1A", "Matematica"));

        ArgumentCaptor<SchoolClass> captor = ArgumentCaptor.forClass(SchoolClass.class);
        verify(classRepository).save(captor.capture());
        assertThat(captor.getValue().getCourse()).isEqualTo("1A");
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void registerAttendancePersistsWhenClassAndStudentAreValid() {
        SchoolClass schoolClass = schoolClass(1L, "1A", "Matematica");
        Attendance saved = attendance(10L, schoolClass, 5L, AttendanceStatus.PRESENT);
        when(classRepository.findById(1L)).thenReturn(Optional.of(schoolClass));
        when(attendanceRepository.existsBySchoolClassIdAndStudentId(1L, 5L)).thenReturn(false);
        when(attendanceRepository.save(org.mockito.ArgumentMatchers.any(Attendance.class))).thenReturn(saved);

        AttendanceResponse response = service.registerAttendance(attendanceRequest(1L, 5L, AttendanceStatus.PRESENT));

        verify(studentClient).findStudentById(5L);
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.status()).isEqualTo(AttendanceStatus.PRESENT);
    }

    @Test
    void registerAttendanceRejectsMissingClass() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registerAttendance(attendanceRequest(1L, 5L, AttendanceStatus.PRESENT)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("clase");
    }

    @Test
    void registerAttendanceRejectsDuplicatedStudentForClass() {
        when(classRepository.findById(1L)).thenReturn(Optional.of(schoolClass(1L, "1A", "Matematica")));
        when(attendanceRepository.existsBySchoolClassIdAndStudentId(1L, 5L)).thenReturn(true);

        assertThatThrownBy(() -> service.registerAttendance(attendanceRequest(1L, 5L, AttendanceStatus.PRESENT)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("ya tiene asistencia");
    }

    @Test
    void getSummaryReturnsStatusCounts() {
        when(attendanceRepository.countByStudentIdAndStatus(5L, AttendanceStatus.PRESENT)).thenReturn(3L);
        when(attendanceRepository.countByStudentIdAndStatus(5L, AttendanceStatus.ABSENT)).thenReturn(1L);
        when(attendanceRepository.countByStudentIdAndStatus(5L, AttendanceStatus.JUSTIFIED)).thenReturn(2L);

        AttendanceSummaryResponse response = service.getSummary(5L);

        assertThat(response.present()).isEqualTo(3L);
        assertThat(response.absent()).isEqualTo(1L);
        assertThat(response.justified()).isEqualTo(2L);
    }

    @Test
    void findClassesFiltersByCourseWhenProvided() {
        when(classRepository.findByCourseIgnoreCase("1A")).thenReturn(List.of(schoolClass(1L, "1A", "Matematica")));

        var responses = service.findClasses("1A");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).course()).isEqualTo("1A");
    }

    private ClassRequest classRequest(String course, String subject) {
        ClassRequest request = new ClassRequest();
        request.setCourse(course);
        request.setSubject(subject);
        request.setClassDate(LocalDate.of(2026, 5, 13));
        return request;
    }

    private AttendanceRequest attendanceRequest(Long classId, Long studentId, AttendanceStatus status) {
        AttendanceRequest request = new AttendanceRequest();
        request.setClassId(classId);
        request.setStudentId(studentId);
        request.setStatus(status);
        request.setObservation("Registro de prueba");
        return request;
    }

    private SchoolClass schoolClass(Long id, String course, String subject) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(id);
        schoolClass.setCourse(course);
        schoolClass.setSubject(subject);
        schoolClass.setClassDate(LocalDate.of(2026, 5, 13));
        return schoolClass;
    }

    private Attendance attendance(Long id, SchoolClass schoolClass, Long studentId, AttendanceStatus status) {
        Attendance attendance = new Attendance();
        attendance.setId(id);
        attendance.setSchoolClass(schoolClass);
        attendance.setStudentId(studentId);
        attendance.setStatus(status);
        attendance.setObservation("Registro de prueba");
        return attendance;
    }
}
