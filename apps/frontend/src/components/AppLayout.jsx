import { Link, NavLink, Outlet } from 'react-router-dom';

export default function AppLayout() {
  return (
    <div className="app-shell">
      <aside className="sidebar">
        <Link to="/panel" className="brand">
          <span className="brand-seal">BOH</span>
          <div>
            <strong>AulaBOH</strong>
            <small>Panel Administrativo</small>
          </div>
        </Link>

        <nav className="sidebar-nav">
          <NavLink to="/panel">
            Panel
          </NavLink>
          <NavLink to="/estudiantes">
            Estudiantes
          </NavLink>
          <NavLink to="/gestion-academica">
            Gestión académica
          </NavLink>
          <NavLink to="/resumen-academico">
            Resumen académico
          </NavLink>
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