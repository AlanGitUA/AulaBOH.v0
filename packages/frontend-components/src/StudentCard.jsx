export default function StudentCard({ student }) {
  if (!student) {
    return null;
  }

  const fullName = `${student.firstName ?? ''} ${student.lastName ?? ''}`.trim();
  const initials = `${student.firstName?.[0] ?? ''}${student.lastName?.[0] ?? ''}`.toUpperCase();

  return (
    <article className="student-profile-card">
      <div className="student-avatar" aria-hidden="true">
        {initials || 'BOH'}
      </div>

      <div className="student-profile-content">
        <p className="eyebrow">Estudiante seleccionado</p>
        <h3>{fullName || 'Estudiante sin nombre'}</h3>

        <div className="student-meta">
          <span>
            Curso: <strong>{student.course ?? 'Sin curso'}</strong>
          </span>
          <span>
            Estado: <strong>{student.status ?? 'Sin estado'}</strong>
          </span>
        </div>
      </div>
    </article>
  );
}