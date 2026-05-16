import { Link, NavLink, Outlet } from 'react-router-dom';

const keycloakLoginUrl =
  import.meta.env.VITE_KEYCLOAK_LOGIN_URL || '/panel';

export default function PublicLayout() {
  return (
    <div className="public-site">
      <header className="public-header">
        <Link to="/" className="public-brand">
          <span className="brand-seal">BOH</span>
          <div>
            <strong>Colegio Bernardo O’Higgins</strong>
            <small>AulaBOH · Libro de Clases Digital</small>
          </div>
        </Link>

        <nav className="public-nav">
          <NavLink to="/" end>Inicio</NavLink>
          <NavLink to="/nosotros">Nosotros</NavLink>
          <NavLink to="/niveles-educativos">Niveles</NavLink>
          <NavLink to="/convivencia-escolar">Convivencia</NavLink>
        </nav>

        <a className="login-button" href={keycloakLoginUrl}>
          Iniciar sesión
        </a>
      </header>

      <Outlet />

      <footer className="public-footer">
        <strong>Colegio Bernardo O’Higgins</strong>
        <span>Plataforma AulaBOH · Desarrollo Fullstack III</span>
      </footer>
    </div>
  );
}