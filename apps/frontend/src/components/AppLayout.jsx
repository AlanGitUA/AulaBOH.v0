import { Link, NavLink, Outlet } from 'react-router-dom';

export default function AppLayout() {
  return (
    <div className="app-shell">
      <aside className="sidebar">
        <Link to="/" className="brand">
          <span className="brand-seal">BOH</span>
          <div>
            <strong>AulaBOH</strong>
            <small>Libro de Clases Digital</small>
          </div>
        </Link>

        <nav className="sidebar-nav">
          <NavLink to="/" end>
            Inicio
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
        </nav>
      </aside>

      <div className="content-shell">
        <header className="topbar">
          <div>
            <p className="eyebrow">Colegio Bernardo O’Higgins</p>
            <h1>Plataforma de Libro de Clases Digital</h1>
          </div>
        </header>

        <Outlet />
      </div>
    </div>
  );
}