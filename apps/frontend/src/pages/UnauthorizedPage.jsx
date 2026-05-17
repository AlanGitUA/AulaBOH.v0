import { Link } from 'react-router-dom';
import { useAuth } from '../auth/AuthProvider';

export default function UnauthorizedPage() {
  const { roles } = useAuth();

  return (
    <main className="page">
      <section className="card unauthorized-card">
        <p className="eyebrow">Acceso restringido</p>
        <h2>No tienes permisos para acceder a esta sección</h2>
        <p>
          Tu usuario está autenticado en Keycloak, pero el rol asignado no permite
          ingresar a este módulo del sistema.
        </p>
        <p className="helper-text">
          Roles detectados: {roles?.length ? roles.join(', ') : 'sin roles asignados'}
        </p>
        <Link className="primary-button" to="/redirigir-rol">
          Volver a mi panel permitido
        </Link>
      </section>
    </main>
  );
}
