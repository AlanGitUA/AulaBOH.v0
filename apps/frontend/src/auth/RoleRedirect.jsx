import { Navigate } from 'react-router-dom';
import { useAuth } from './AuthProvider';

export default function RoleRedirect() {
  const { initialized, authenticated, hasRole } = useAuth();

  if (!initialized) {
    return (
      <main className="page">
        <section className="card">
          <h2>Cargando sesión...</h2>
          <p>Validando rol asignado en Keycloak.</p>
        </section>
      </main>
    );
  }

  if (!authenticated) {
    return <Navigate to="/" replace />;
  }

  if (hasRole('ADMIN')) {
    return <Navigate to="/panel" replace />;
  }

  if (hasRole('DOCENTE')) {
    return <Navigate to="/gestion-academica" replace />;
  }

  if (hasRole('ESTUDIANTE')) {
    return <Navigate to="/resumen-academico" replace />;
  }

  if (hasRole('APODERADO')) {
    return <Navigate to="/resumen-academico" replace />;
  }

  return <Navigate to="/no-autorizado" replace />;
}
