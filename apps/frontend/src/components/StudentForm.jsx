import { useState } from 'react';
import { studentApi } from '../services/studentApi';
import { COURSE_OPTIONS } from '../utils/courseCatalog';

const initialForm = {
  firstName: '',
  lastName: '',
  course: '',
  email: '',
  studentUsername: '',
  guardianUsername: '',
};

export default function StudentForm({ editingStudent, onSaved, onCancel }) {
  const [form, setForm] = useState(editingStudent ?? initialForm);
  const [message, setMessage] = useState('');

  const update = (event) => setForm({ ...form, [event.target.name]: event.target.value });

  const submit = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      if (editingStudent) {
        await studentApi.update(editingStudent.id, form);
        setMessage('Estudiante actualizado correctamente.');
      } else {
        await studentApi.create(form);
        setForm(initialForm);
        setMessage('Estudiante registrado correctamente.');
      }
      onSaved?.();
    } catch (error) {
      console.error(error);
      setMessage(editingStudent
        ? 'No se pudo actualizar el estudiante.'
        : 'No se pudo registrar el estudiante.');
    }
  };

  return (
    <form className="card form" onSubmit={submit}>
      <h2>{editingStudent ? 'Editar estudiante' : 'Registrar estudiante'}</h2>
      <input name="firstName" placeholder="Nombre" value={form.firstName} onChange={update} required />
      <input name="lastName" placeholder="Apellido" value={form.lastName} onChange={update} required />
      <input name="course" list="course-options" placeholder="Curso" value={form.course} onChange={update} required />
      <datalist id="course-options">
        {COURSE_OPTIONS.map((course) => (
          <option key={course} value={course} />
        ))}
      </datalist>
      <input name="email" type="email" placeholder="Correo" value={form.email} onChange={update} />
      <input name="studentUsername" placeholder="Usuario Keycloak del estudiante" value={form.studentUsername} onChange={update} />
      <input name="guardianUsername" placeholder="Usuario Keycloak del apoderado" value={form.guardianUsername} onChange={update} />
      <div className="form-actions">
        <button type="submit">{editingStudent ? 'Actualizar' : 'Guardar'}</button>
        {editingStudent && (
          <button type="button" className="secondary-button" onClick={onCancel}>
            Cancelar
          </button>
        )}
      </div>
      {message && <small>{message}</small>}
    </form>
  );
}
