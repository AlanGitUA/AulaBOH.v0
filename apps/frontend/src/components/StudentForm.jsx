import { useState } from 'react';
import { studentApi } from '../services/studentApi';

const initialForm = { firstName: '', lastName: '', course: '', email: '' };

export default function StudentForm({ onCreated }) {
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState('');

  const update = (event) => setForm({ ...form, [event.target.name]: event.target.value });

  const submit = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      await studentApi.create(form);
      setForm(initialForm);
      setMessage('Estudiante registrado correctamente.');
      onCreated?.();
    } catch (error) {
      console.error(error);
      setMessage('No se pudo registrar el estudiante. Revisa que BFF y Students Service estén activos.');
    }
  };

  return (
    <form className="card form" onSubmit={submit}>
      <h2>Registrar estudiante</h2>
      <input name="firstName" placeholder="Nombre" value={form.firstName} onChange={update} required />
      <input name="lastName" placeholder="Apellido" value={form.lastName} onChange={update} required />
      <input name="course" placeholder="Curso" value={form.course} onChange={update} required />
      <input name="email" type="email" placeholder="Correo" value={form.email} onChange={update} />
      <button type="submit">Guardar</button>
      {message && <small>{message}</small>}
    </form>
  );
}
