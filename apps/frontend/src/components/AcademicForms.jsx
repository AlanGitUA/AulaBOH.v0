import { useState } from 'react';
import { bffApi } from '../services/bffApi';

const today = new Date().toISOString().slice(0, 10);

const modules = [
  {
    id: 'class',
    title: 'Registrar clase',
    description: 'Crea clases por asignatura, curso y fecha.',
  },
  {
    id: 'attendance',
    title: 'Registrar asistencia',
    description: 'Asocia estudiantes a una clase con estado de asistencia.',
  },
  {
    id: 'evaluation',
    title: 'Registrar evaluación',
    description: 'Registra evaluaciones académicas por curso.',
  },
  {
    id: 'grade',
    title: 'Registrar calificación',
    description: 'Asigna una nota a un estudiante según evaluación.',
  },
];

export default function AcademicForms({ students, classes, evaluations, onChanged }) {
  const [activeModule, setActiveModule] = useState('class');
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

  const activeModuleData = modules.find((module) => module.id === activeModule);

  return (
    <section className="academic-workspace">
      <div className="module-switcher">
        {modules.map((module) => (
          <button
            key={module.id}
            type="button"
            className={`module-switch-button ${activeModule === module.id ? 'active' : ''}`}
            onClick={() => {
              setActiveModule(module.id);
              setMessage('');
            }}
          >
            <span>{module.title}</span>
            <small>{module.description}</small>
          </button>
        ))}
      </div>

      <section className="card academic-form-card">
        <div className="form-heading">
          <p className="eyebrow">Módulo seleccionado</p>
          <h2>{activeModuleData?.title}</h2>
          <p>{activeModuleData?.description}</p>
        </div>

        {activeModule === 'class' && (
          <form className="form" onSubmit={createClass}>
            <input name="subject" placeholder="Asignatura" value={classForm.subject} onChange={updateClass} required />
            <input name="course" placeholder="Curso" value={classForm.course} onChange={updateClass} required />
            <input name="classDate" type="date" value={classForm.classDate} onChange={updateClass} required />
            <button type="submit">Guardar clase</button>
          </form>
        )}

        {activeModule === 'attendance' && (
          <form className="form" onSubmit={createAttendance}>
            <select name="studentId" value={attendanceForm.studentId} onChange={updateAttendance} required>
              <option value="">Selecciona estudiante</option>
              {students.map((student) => (
                <option key={student.id} value={student.id}>
                  {student.firstName} {student.lastName}
                </option>
              ))}
            </select>

            <select name="classId" value={attendanceForm.classId} onChange={updateAttendance} required>
              <option value="">Selecciona clase</option>
              {classes.map((schoolClass) => (
                <option key={schoolClass.id} value={schoolClass.id}>
                  {schoolClass.subject} - {schoolClass.course}
                </option>
              ))}
            </select>

            <select name="status" value={attendanceForm.status} onChange={updateAttendance} required>
              <option value="PRESENT">PRESENTE</option>
              <option value="ABSENT">AUSENTE</option>
              <option value="JUSTIFIED">JUSTIFICADO</option>
            </select>

            <input name="observation" placeholder="Observación" value={attendanceForm.observation} onChange={updateAttendance} />
            <button type="submit">Guardar asistencia</button>

            {(!students.length || !classes.length) && (
              <p className="helper-text">
                Para registrar asistencia primero deben existir estudiantes y clases.
              </p>
            )}
          </form>
        )}

        {activeModule === 'evaluation' && (
          <form className="form" onSubmit={createEvaluation}>
            <input name="title" placeholder="Título de la evaluación" value={evaluationForm.title} onChange={updateEvaluation} required />
            <input name="subject" placeholder="Asignatura" value={evaluationForm.subject} onChange={updateEvaluation} required />
            <input name="course" placeholder="Curso" value={evaluationForm.course} onChange={updateEvaluation} required />
            <input name="evaluationDate" type="date" value={evaluationForm.evaluationDate} onChange={updateEvaluation} />
            <button type="submit">Guardar evaluación</button>
          </form>
        )}

        {activeModule === 'grade' && (
          <form className="form" onSubmit={createGrade}>
            <select name="studentId" value={gradeForm.studentId} onChange={updateGrade} required>
              <option value="">Selecciona estudiante</option>
              {students.map((student) => (
                <option key={student.id} value={student.id}>
                  {student.firstName} {student.lastName}
                </option>
              ))}
            </select>

            <select name="evaluationId" value={gradeForm.evaluationId} onChange={updateGrade} required>
              <option value="">Selecciona evaluación</option>
              {evaluations.map((evaluation) => (
                <option key={evaluation.id} value={evaluation.id}>
                  {evaluation.title} - {evaluation.subject}
                </option>
              ))}
            </select>

            <input name="score" type="number" step="0.1" min="1" max="7" placeholder="Nota" value={gradeForm.score} onChange={updateGrade} required />
            <button type="submit">Guardar calificación</button>

            {(!students.length || !evaluations.length) && (
              <p className="helper-text">
                Para registrar calificaciones primero deben existir estudiantes y evaluaciones.
              </p>
            )}
          </form>
        )}

        {message && <strong className="status-message">{message}</strong>}
      </section>
    </section>
  );
}