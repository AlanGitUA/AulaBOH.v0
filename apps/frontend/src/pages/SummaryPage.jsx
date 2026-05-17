import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { StudentCard, AttendanceBadge } from '@aulaboh/frontend-components';
import { bffApi } from '../services/bffApi';
import { useAuth } from '../auth/AuthProvider';

export default function SummaryPage() {
  const location = useLocation();
  const { hasRole } = useAuth();
  const initialStudentId = location.state?.studentId ?? '';
  const isStudent = hasRole('ESTUDIANTE');
  const isGuardian = hasRole('APODERADO');
  const hasGeneralAccess = hasRole('ADMIN') || hasRole('DOCENTE');

  const [students, setStudents] = useState([]);
  const [selectedStudentId, setSelectedStudentId] = useState(initialStudentId);
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(false);

  const loadStudents = async () => {
    try {
      const data = await bffApi.listStudents();
      setStudents(data ?? []);
    } catch (error) {
      console.error(error);
      setStudents([]);
    }
  };

  const loadGuardianStudents = async () => {
    try {
      const data = await bffApi.myStudents();
      setStudents(data ?? []);
    } catch (error) {
      console.error(error);
      setStudents([]);
    }
  };

  const showSummary = async (studentId) => {
    if (!studentId) {
      setSummary(null);
      return;
    }

    setLoading(true);

    try {
      const data = isGuardian
        ? await bffApi.myStudentSummary(studentId)
        : await bffApi.studentSummary(studentId);
      setSummary(data);
    } catch (error) {
      console.error(error);
      setSummary(null);
      alert('No se pudo cargar el resumen. Revisa que Attendance Service y Grades Service esten activos.');
    } finally {
      setLoading(false);
    }
  };

  const loadOwnSummary = async () => {
    setLoading(true);
    try {
      const data = await bffApi.ownStudentSummary();
      setSummary(data);
      setSelectedStudentId(data?.student?.id ?? '');
    } catch (error) {
      console.error(error);
      setSummary(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isStudent) {
      loadOwnSummary();
      return;
    }

    if (isGuardian) {
      loadGuardianStudents();
      return;
    }

    loadStudents();
  }, []);

  useEffect(() => {
    if (!isStudent && selectedStudentId) {
      showSummary(selectedStudentId);
    }
  }, [selectedStudentId]);

  return (
    <main className="page">
      <div className="page-header">
        <div>
          <p className="eyebrow">Consulta academica</p>
          <h2>Resumen academico del estudiante</h2>
          <p>Visualiza asistencia y calificaciones consolidadas mediante el BFF.</p>
        </div>

        <Link to="/" className="secondary-button">
          ← Volver al inicio
        </Link>
      </div>

      <section className="summary-layout">
        <section className="card">
          {isStudent ? (
            <>
              <h2>Mi resumen</h2>
              <p className="helper-text">La informacion corresponde al estudiante asociado a tu cuenta.</p>
            </>
          ) : (
            <>
              <h2>{isGuardian ? 'Seleccionar representado' : 'Seleccionar estudiante'}</h2>

              <select
                value={selectedStudentId}
                onChange={(event) => setSelectedStudentId(event.target.value)}
              >
                <option value="">{isGuardian ? 'Selecciona representado' : 'Selecciona estudiante'}</option>
                {students.map((student) => (
                  <option key={student.id} value={student.id}>
                    {student.firstName} {student.lastName} - {student.course}
                  </option>
                ))}
              </select>

              {!students.length && (
                <p className="helper-text">
                  {hasGeneralAccess
                    ? 'Primero registra estudiantes en el modulo correspondiente.'
                    : 'No hay estudiantes asociados a tu cuenta.'}
                </p>
              )}
            </>
          )}
        </section>

        <section className="card summary-card">
          <h2>Detalle academico</h2>

          {loading && <p>Cargando resumen...</p>}

          {!loading && !summary && (
            <p>{isStudent ? 'No existe un estudiante asociado a tu cuenta.' : 'Selecciona un estudiante para ver asistencia y calificaciones.'}</p>
          )}

          {!loading && summary && (
            <div>
              <StudentCard student={summary.student} />

              <div className="stats">
                <AttendanceBadge label="Presentes" value={summary.attendance?.present ?? 0} tone="success" />
                <AttendanceBadge label="Ausentes" value={summary.attendance?.absent ?? 0} tone="danger" />
                <AttendanceBadge label="Justificados" value={summary.attendance?.justified ?? 0} tone="warning" />
              </div>

              <h3>Calificaciones</h3>

              {summary.grades?.length ? (
                <ul className="grades-list">
                  {summary.grades.map((grade) => (
                    <li key={grade.id}>
                      {grade.subject} - {grade.title}: <strong>{grade.score}</strong>
                    </li>
                  ))}
                </ul>
              ) : (
                <p>Sin calificaciones registradas.</p>
              )}
            </div>
          )}
        </section>
      </section>
    </main>
  );
}
