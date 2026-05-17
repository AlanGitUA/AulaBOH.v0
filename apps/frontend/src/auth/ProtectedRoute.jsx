import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from './AuthProvider';

export default function ProtectedRoute({ allowedRoles = [], children }) {
  const location = useLocation();
  const { initialized, authenticated, hasAnyRole } = useAuth();

  if (!initialized) {
    return (
      <main className="page">
        <section className="card">
          <h2>Cargando sesión...</h2>
          <p>Validando credenciales con Keycloak.</p>
        </section>
      </main>
    );
  }

  if (!authenticated) {
    return <Navigate to="/" replace state={{ from: location.pathname }} />;
  }

  if (allowedRoles.length > 0 && !hasAnyRole(allowedRoles)) {
    return <Navigate to="/no-autorizado" replace />;
  }

  return children;
}
