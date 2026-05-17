import { Link, NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../auth/AuthProvider';

export default function AppLayout() {
  const { authenticated, fullName, roles, hasRole, hasAnyRole, logout } = useAuth();

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <Link to="/redirigir-rol" className="brand">
          <span className="brand-seal">BOH</span>
          <div>
            <strong>AulaBOH</strong>
            <small>Panel según rol</small>
          </div>
        </Link>

        {authenticated && (
          <div className="session-box">
            <span>Sesión iniciada</span>
            <strong>{fullName}</strong>
            <small>{roles?.filter((role) => ['ADMIN', 'DOCENTE', 'ESTUDIANTE', 'APODERADO'].includes(role)).join(', ')}</small>
          </div>
        )}

        <nav className="sidebar-nav">
          {authenticated && hasAnyRole(['ADMIN', 'DOCENTE']) && (
            <NavLink to="/panel">
              Panel
            </NavLink>
          )}

          {authenticated && hasRole('ADMIN') && (
            <NavLink to="/estudiantes">
              Estudiantes
            </NavLink>
          )}

          {authenticated && hasAnyRole(['ADMIN', 'DOCENTE']) && (
            <NavLink to="/gestion-academica">
              Gestión académica
            </NavLink>
          )}

          {authenticated && hasAnyRole(['ADMIN', 'DOCENTE', 'ESTUDIANTE', 'APODERADO']) && (
            <NavLink to="/resumen-academico">
              Resumen académico
            </NavLink>
          )}

          {authenticated && (
            <button className="sidebar-logout" type="button" onClick={logout}>
              Cerrar sesión
            </button>
          )}

          <Link to="/" className="sidebar-public-link">
            Salir al inicio
          </Link>
        </nav>
      </aside>

      <div className="content-shell">
        <header className="topbar">
          <div>
            <p className="eyebrow">Colegio Bernardo O’Higgins</p>
            <h1>Panel de administración académica</h1>
          </div>
        </header>

        <Outlet />
      </div>
    </div>
  );
}
