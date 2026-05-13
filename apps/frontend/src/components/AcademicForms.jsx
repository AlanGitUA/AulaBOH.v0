import { useState } from 'react';
import { bffApi } from '../services/bffApi';

const today = new Date().toISOString().slice(0, 10);

export default function AcademicForms({ students, classes, evaluations, onChanged }) {
  const [classForm, setClassForm] = useState({ subject: '', course: '', classDate: today });
  const [attendanceForm, setAttendanceForm] = useState({ studentId: '', classId: '', status: 'PRESENT', observation: '' });
  const [evaluationForm, setEvaluationForm] = useState({ title: '', subject: '', course: '', evaluationDate: today });
  const [gradeForm, setGradeForm] = useState({ studentId: '', evaluationId: '', score: '' });
  const [message, setMessage] = useState('');

  const updateClass = (event) => setClassForm({ ...classForm, [event.target.name]: event.target.value });
  const updateAttendance = (event) => setAttendanceForm({ ...attendanceForm, [event.target.name]: event.target.value });
  const updateEvaluation = (event) => setEvaluationForm({ ...evaluationForm, [event.target.name]: event.target.value });
  const updateGrade = (event) => setGradeForm({ ...gradeForm, [event.target.name]: event.target.value });

  const createClass = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      await bffApi.createClass(classForm);
      setClassForm({ subject: '', course: '', classDate: today });
      setMessage('Clase registrada correctamente.');
      onChanged?.();
    } catch (error) {
      console.error(error);
      setMessage('No se pudo registrar la clase. Revisa que BFF y Attendance Service estén activos.');
    }
  };

  const createAttendance = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      await bffApi.createAttendance({
        ...attendanceForm,
        studentId: Number(attendanceForm.studentId),
        classId: Number(attendanceForm.classId),
      });
      setAttendanceForm({ studentId: '', classId: '', status: 'PRESENT', observation: '' });
      setMessage('Asistencia registrada correctamente.');
      onChanged?.();
    } catch (error) {
      console.error(error);
      setMessage('No se pudo registrar la asistencia. Verifica estudiante, clase y que no esté duplicada.');
    }
  };

  const createEvaluation = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      await bffApi.createEvaluation(evaluationForm);
      setEvaluationForm({ title: '', subject: '', course: '', evaluationDate: today });
      setMessage('Evaluación registrada correctamente.');
      onChanged?.();
    } catch (error) {
      console.error(error);
      setMessage('No se pudo registrar la evaluación. Revisa que BFF y Grades Service estén activos.');
    }
  };

  const createGrade = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      await bffApi.createGrade({
        studentId: Number(gradeForm.studentId),
        evaluationId: Number(gradeForm.evaluationId),
        score: Number(gradeForm.score),
      });
      setGradeForm({ studentId: '', evaluationId: '', score: '' });
      setMessage('Calificación registrada correctamente.');
      onChanged?.();
    } catch (error) {
      console.error(error);
      setMessage('No se pudo registrar la calificación. La nota debe estar entre 1.0 y 7.0.');
    }
  };

  return (
    <section className="card academic-panel">
      <h2>Gestión académica</h2>
      <p>Registra clases, asistencia, evaluaciones y calificaciones desde el frontend usando el BFF.</p>
      <div className="academic-grid">
        <form className="mini-card form" onSubmit={createClass}>
          <h3>Registrar clase</h3>
          <input name="subject" placeholder="Asignatura" value={classForm.subject} onChange={updateClass} required />
          <input name="course" placeholder="Curso" value={classForm.course} onChange={updateClass} required />
          <input name="classDate" type="date" value={classForm.classDate} onChange={updateClass} required />
          <button type="submit">Guardar clase</button>
        </form>

        <form className="mini-card form" onSubmit={createAttendance}>
          <h3>Registrar asistencia</h3>
          <select name="studentId" value={attendanceForm.studentId} onChange={updateAttendance} required>
            <option value="">Selecciona estudiante</option>
            {students.map((student) => (
              <option key={student.id} value={student.id}>{student.firstName} {student.lastName}</option>
            ))}
          </select>
          <select name="classId" value={attendanceForm.classId} onChange={updateAttendance} required>
            <option value="">Selecciona clase</option>
            {classes.map((schoolClass) => (
              <option key={schoolClass.id} value={schoolClass.id}>{schoolClass.subject} - {schoolClass.course}</option>
            ))}
          </select>
          <select name="status" value={attendanceForm.status} onChange={updateAttendance} required>
            <option value="PRESENT">PRESENT</option>
            <option value="ABSENT">ABSENT</option>
            <option value="JUSTIFIED">JUSTIFIED</option>
          </select>
          <input name="observation" placeholder="Observación" value={attendanceForm.observation} onChange={updateAttendance} />
          <button type="submit">Guardar asistencia</button>
        </form>

        <form className="mini-card form" onSubmit={createEvaluation}>
          <h3>Registrar evaluación</h3>
          <input name="title" placeholder="Título" value={evaluationForm.title} onChange={updateEvaluation} required />
          <input name="subject" placeholder="Asignatura" value={evaluationForm.subject} onChange={updateEvaluation} required />
          <input name="course" placeholder="Curso" value={evaluationForm.course} onChange={updateEvaluation} required />
          <input name="evaluationDate" type="date" value={evaluationForm.evaluationDate} onChange={updateEvaluation} />
          <button type="submit">Guardar evaluación</button>
        </form>

        <form className="mini-card form" onSubmit={createGrade}>
          <h3>Registrar calificación</h3>
          <select name="studentId" value={gradeForm.studentId} onChange={updateGrade} required>
            <option value="">Selecciona estudiante</option>
            {students.map((student) => (
              <option key={student.id} value={student.id}>{student.firstName} {student.lastName}</option>
            ))}
          </select>
          <select name="evaluationId" value={gradeForm.evaluationId} onChange={updateGrade} required>
            <option value="">Selecciona evaluación</option>
            {evaluations.map((evaluation) => (
              <option key={evaluation.id} value={evaluation.id}>{evaluation.title} - {evaluation.subject}</option>
            ))}
          </select>
          <input name="score" type="number" step="0.1" min="1" max="7" placeholder="Nota" value={gradeForm.score} onChange={updateGrade} required />
          <button type="submit">Guardar calificación</button>
        </form>
      </div>
      {message && <strong className="status-message">{message}</strong>}
    </section>
  );
}
