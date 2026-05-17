import { Link } from 'react-router-dom';
import { useAuth } from '../auth/AuthProvider';

const modules = [
  {
    title: 'Agregar estudiante',
    description: 'Registra estudiantes y consulta el listado general del curso.',
    path: '/estudiantes',
    tag: 'Gestión de estudiantes',
    allowedRoles: ['ADMIN'],
  },
  {
    title: 'Gestión académica',
    description: 'Registra clases, asistencia, evaluaciones y calificaciones.',
    path: '/gestion-academica',
    tag: 'Clases y notas',
    allowedRoles: ['ADMIN', 'DOCENTE'],
  },
  {
    title: 'Resumen académico',
    description: 'Consulta asistencia y calificaciones consolidadas por estudiante.',
    path: '/resumen-academico',
    tag: 'Consulta BFF',
    allowedRoles: ['ADMIN', 'DOCENTE', 'ESTUDIANTE', 'APODERADO'],
  },
];

export default function DashboardPage() {
  const { hasAnyRole, fullName } = useAuth();
  const visibleModules = modules.filter((module) => hasAnyRole(module.allowedRoles));

  return (
    <main className="page">
      <section className="hero-card">
        <div>
          <p className="eyebrow">Panel institucional</p>
          <h2>Accesos principales del sistema</h2>
          <p>
            Bienvenido/a {fullName}. Desde este panel puedes navegar solo a los
            módulos permitidos según tu rol de Keycloak.
          </p>
        </div>

        <div className="hero-stamp">
          <span>Panel</span>
          <strong>BOH</strong>
        </div>
      </section>

      <section className="module-grid">
        {visibleModules.map((module) => (
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
