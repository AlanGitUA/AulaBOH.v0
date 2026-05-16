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
          <p className="eyebrow">Panel institucional</p>
          <h2>Accesos principales del sistema</h2>
          <p>
            Desde este panel puedes navegar rápidamente a los módulos principales
            del libro de clases digital AulaBOH.
          </p>
        </div>

        <div className="hero-stamp">
          <span>Panel</span>
          <strong>BOH</strong>
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