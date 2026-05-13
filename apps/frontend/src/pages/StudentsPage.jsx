import { useEffect, useState } from 'react';
import StudentForm from '../components/StudentForm';
import AcademicForms from '../components/AcademicForms';
import { bffApi } from '../services/bffApi';

export default function StudentsPage() {
  const [students, setStudents] = useState([]);
  const [classes, setClasses] = useState([]);
  const [evaluations, setEvaluations] = useState([]);
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    const safeLoad = async (loader, fallback) => {
      try {
        return await loader();
      } catch (error) {
        console.error(error);
        return fallback;
      }
    };

    const [studentsData, classesData, evaluationsData] = await Promise.all([
      safeLoad(bffApi.listStudents, []),
      safeLoad(bffApi.listClasses, []),
      safeLoad(bffApi.listEvaluations, []),
    ]);

    setStudents(studentsData ?? []);
    setClasses(classesData ?? []);
    setEvaluations(evaluationsData ?? []);
  };

  useEffect(() => {
    loadData();
  }, []);

  const showSummary = async (studentId) => {
    setLoading(true);
    try {
      setSummary(await bffApi.studentSummary(studentId));
    } catch (error) {
      console.error(error);
      setSummary(null);
      alert('No se pudo cargar el resumen. Revisa que Attendance Service y Grades Service estén activos.');
    } finally {
      setLoading(false);
    }
  };

  const refresh = async () => {
    await loadData();
    if (summary?.student?.id) {
      try {
        setSummary(await bffApi.studentSummary(summary.student.id));
      } catch (error) {
        console.error(error);
      }
    }
  };

  return (
    <main>
      <section className="layout">
        <StudentForm onCreated={refresh} />

        <section className="card table-card">
          <h2>Estudiantes registrados</h2>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Curso</th>
                <th>Acción</th>
              </tr>
            </thead>
            <tbody>
              {students.map((student) => (
                <tr key={student.id}>
                  <td>{student.id}</td>
                  <td>{student.firstName} {student.lastName}</td>
                  <td>{student.course}</td>
                  <td><button onClick={() => showSummary(student.id)}>Ver resumen</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>

        <section className="card summary-card">
          <h2>Resumen académico</h2>
          {loading && <p>Cargando resumen...</p>}
          {!loading && !summary && <p>Selecciona un estudiante para ver asistencia y calificaciones.</p>}
          {!loading && summary && (
            <div>
              <div className="student-box">
                <h3>{summary.student.firstName} {summary.student.lastName}</h3>
                <p>Curso: {summary.student.course}</p>
                <p>Estado: {summary.student.status}</p>
              </div>
              <div className="stats">
                <span>Presentes <strong>{summary.attendance?.present ?? 0}</strong></span>
                <span>Ausentes <strong>{summary.attendance?.absent ?? 0}</strong></span>
                <span>Justificados <strong>{summary.attendance?.justified ?? 0}</strong></span>
              </div>
              <h3>Calificaciones</h3>
              {summary.grades?.length ? (
                <ul className="grades-list">
                  {summary.grades.map((grade) => (
                    <li key={grade.id}>{grade.subject} - {grade.title}: <strong>{grade.score}</strong></li>
                  ))}
                </ul>
              ) : <p>Sin calificaciones registradas.</p>}
            </div>
          )}
        </section>
      </section>

      <AcademicForms students={students} classes={classes} evaluations={evaluations} onChanged={refresh} />
    </main>
  );
}
