import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import StudentForm from '../components/StudentForm';
import { bffApi } from '../services/bffApi';

export default function StudentsPage() {
  const [students, setStudents] = useState([]);

  const loadStudents = async () => {
    try {
      const data = await bffApi.listStudents();
      setStudents(data ?? []);
    } catch (error) {
      console.error(error);
      setStudents([]);
    }
  };

  useEffect(() => {
    loadStudents();
  }, []);

  return (
    <main className="page">
      <div className="page-header">
        <div>
          <p className="eyebrow">Módulo de estudiantes</p>
          <h2>Registro de estudiantes</h2>
          <p>
            Administra la información base de los estudiantes del establecimiento.
          </p>
        </div>

        <Link to="/" className="secondary-button">
          ← Volver al inicio
        </Link>
      </div>

      <section className="two-column-layout">
        <StudentForm onCreated={loadStudents} />

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
              {students.length ? (
                students.map((student) => (
                  <tr key={student.id}>
                    <td>{student.id}</td>
                    <td>{student.firstName} {student.lastName}</td>
                    <td>{student.course}</td>
                    <td>
                      <Link
                        className="table-link"
                        to="/resumen-academico"
                        state={{ studentId: student.id }}
                      >
                        Ver resumen
                      </Link>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="empty-table">
                    No hay estudiantes registrados todavía.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </section>
      </section>
    </main>
  );
}