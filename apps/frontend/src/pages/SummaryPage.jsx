import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { StudentCard, AttendanceBadge } from '@aulaboh/frontend-components';
import { bffApi } from '../services/bffApi';

export default function SummaryPage() {
  const location = useLocation();
  const initialStudentId = location.state?.studentId ?? '';

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

  const showSummary = async (studentId) => {
    if (!studentId) {
      setSummary(null);
      return;
    }

    setLoading(true);

    try {
      const data = await bffApi.studentSummary(studentId);
      setSummary(data);
    } catch (error) {
      console.error(error);
      setSummary(null);
      alert('No se pudo cargar el resumen. Revisa que Attendance Service y Grades Service estén activos.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadStudents();
  }, []);

  useEffect(() => {
    if (selectedStudentId) {
      showSummary(selectedStudentId);
    }
  }, [selectedStudentId]);

  return (
    <main className="page">
      <div className="page-header">
        <div>
          <p className="eyebrow">Consulta académica</p>
          <h2>Resumen académico del estudiante</h2>
          <p>
            Visualiza asistencia y calificaciones consolidadas mediante el BFF.
          </p>
        </div>

        <Link to="/" className="secondary-button">
          ← Volver al inicio
        </Link>
      </div>

      <section className="summary-layout">
        <section className="card">
          <h2>Seleccionar estudiante</h2>

          <select
            value={selectedStudentId}
            onChange={(event) => setSelectedStudentId(event.target.value)}
          >
            <option value="">Selecciona estudiante</option>
            {students.map((student) => (
              <option key={student.id} value={student.id}>
                {student.firstName} {student.lastName} - {student.course}
              </option>
            ))}
          </select>

          {!students.length && (
            <p className="helper-text">
              Primero registra estudiantes en el módulo correspondiente.
            </p>
          )}
        </section>

        <section className="card summary-card">
          <h2>Detalle académico</h2>

          {loading && <p>Cargando resumen...</p>}

          {!loading && !summary && (
            <p>Selecciona un estudiante para ver asistencia y calificaciones.</p>
          )}

          {!loading && summary && (
            <div>
              <StudentCard student={summary.student} />

              <div className="stats">
                <AttendanceBadge
                  label="Presentes"
                  value={summary.attendance?.present ?? 0}
                  tone="success"
                />
                <AttendanceBadge
                  label="Ausentes"
                  value={summary.attendance?.absent ?? 0}
                  tone="danger"
                />
                <AttendanceBadge
                  label="Justificados"
                  value={summary.attendance?.justified ?? 0}
                  tone="warning"
                />
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