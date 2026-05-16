import { Link } from 'react-router-dom';

const modules = [
  {
    title: 'Agregar estudiante',
    description: 'Registra estudiantes y consulta el listado general del curso.',
    path: '/estudiantes',
    tag: 'Gestión de estudiantes',
  },
  {
    title: 'Gestión académica',
    description: 'Registra clases, asistencia, evaluaciones y calificaciones.',
    path: '/gestion-academica',
    tag: 'Clases y notas',
  },
  {
    title: 'Resumen académico',
    description: 'Consulta asistencia y calificaciones consolidadas por estudiante.',
    path: '/resumen-academico',
    tag: 'Consulta BFF',
  },
];

export default function DashboardPage() {
  return (
    <main className="page">
      <section className="hero-card">
        <div>
          <p className="eyebrow">Primera versión funcional</p>
          <h2>Libro de clases digital AulaBOH</h2>
          <p>
            Sistema académico para registrar estudiantes, asistencia, evaluaciones
            y calificaciones desde una interfaz ordenada e integrada con el BFF.
          </p>
        </div>

        <div className="hero-stamp">
          <span>DSY1106</span>
          <strong>EP2</strong>
        </div>
      </section>

      <section className="module-grid">
        {modules.map((module) => (
          <Link to={module.path} className="module-card" key={module.path}>
            <span>{module.tag}</span>
            <h3>{module.title}</h3>
            <p>{module.description}</p>
            <strong>Entrar →</strong>
          </Link>
        ))}
      </section>
    </main>
  );
}