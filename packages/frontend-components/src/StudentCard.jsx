export default function StudentCard({ student }) {
  if (!student) return null;
  return (
    <article className="student-card">
      <h3>{student.firstName} {student.lastName}</h3>
      <p>Curso: {student.course}</p>
      <p>Estado: {student.status}</p>
    </article>
  );
}
