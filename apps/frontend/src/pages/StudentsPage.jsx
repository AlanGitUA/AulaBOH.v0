import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import StudentForm from '../components/StudentForm';
import { bffApi } from '../services/bffApi';

export default function StudentsPage() {
  const [students, setStudents] = useState([]);
  const [editingStudent, setEditingStudent] = useState(null);
  const [message, setMessage] = useState('');

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

  const handleSaved = async () => {
    await loadStudents();
    setEditingStudent(null);
  };

  const handleDelete = async (studentId) => {
    if (!window.confirm('Confirma que deseas eliminar este estudiante.')) {
      return;
    }
    setMessage('');
    try {
      await bffApi.deleteStudent(studentId);
      await loadStudents();
      if (editingStudent?.id === studentId) {
        setEditingStudent(null);
      }
      setMessage('Estudiante eliminado correctamente.');
    } catch (error) {
      console.error(error);
      setMessage('No se pudo eliminar el estudiante.');
    }
  };

  return (
    <main className="page">
      <div className="page-header">
        <div>
          <p className="eyebrow">Modulo de estudiantes</p>
          <h2>Registro de estudiantes</h2>
          <p>Administra la informacion base de los estudiantes del establecimiento.</p>
        </div>

        <Link to="/" className="secondary-button">
          Volver al inicio
        </Link>
      </div>

      <section className="two-column-layout">
        <StudentForm
          key={editingStudent?.id ?? 'new'}
          editingStudent={editingStudent}
          onSaved={handleSaved}
          onCancel={() => setEditingStudent(null)}
        />

        <section className="card table-card">
          <h2>Estudiantes registrados</h2>

          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Curso</th>
                <th>Acciones</th>
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
                      <div className="table-actions">
                        <Link className="table-link" to="/resumen-academico" state={{ studentId: student.id }}>
                          Ver resumen
                        </Link>
                        <button type="button" className="table-link-button" onClick={() => setEditingStudent(student)}>
                          Editar
                        </button>
                        <button type="button" className="table-link-button danger" onClick={() => handleDelete(student.id)}>
                          Eliminar
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="empty-table">
                    No hay estudiantes registrados todavia.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
          {message && <small>{message}</small>}
        </section>
      </section>
    </main>
  );
}
