import { Link, NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../auth/AuthProvider';

export default function PublicLayout() {
  const { initialized, authenticated, login, logout, fullName } = useAuth();

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

        <div className="auth-actions">
          {initialized && authenticated ? (
            <>
              <Link className="login-button" to="/redirigir-rol">
                Mi panel
              </Link>
              <button className="secondary-auth-button" type="button" onClick={logout}>
                Cerrar sesión
              </button>
              <small className="session-label">{fullName}</small>
            </>
          ) : (
            <button className="login-button" type="button" onClick={login} disabled={!initialized}>
              Iniciar sesión
            </button>
          )}
        </div>
      </header>

      <Outlet />

      <footer className="public-footer">
        <strong>Colegio Bernardo O’Higgins</strong>
        <span>Plataforma AulaBOH · Desarrollo Fullstack III</span>
      </footer>
    </div>
  );
}
