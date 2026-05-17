import { Link } from 'react-router-dom';
import { useAuth } from '../auth/AuthProvider';

const publicCards = [
  {
    title: 'Nosotros',
    description: 'Conoce la identidad, historia y propósito educativo del establecimiento.',
    path: '/nosotros',
    icon: '🏫',
  },
  {
    title: 'Reglamento',
    description: 'Accede a normas, lineamientos y criterios institucionales.',
    path: '/reglamento',
    icon: '📘',
  },
  {
    title: 'Convivencia Escolar',
    description: 'Información sobre bienestar, respeto y vida escolar.',
    path: '/convivencia-escolar',
    icon: '🤝',
  },
  {
    title: 'Nuestro Equipo',
    description: 'Directivos, docentes y profesionales de apoyo del colegio.',
    path: '/equipo',
    icon: '👨‍🏫',
  },
  {
    title: 'Misión, Visión y Valores',
    description: 'Principios que guían nuestro proyecto educativo.',
    path: '/mision-vision-valores',
    icon: '⭐',
  },
  {
    title: 'Niveles Educativos',
    description: 'Cursos de enseñanza media desde 1° a 4° medio.',
    path: '/niveles-educativos',
    icon: '🎓',
  },
];

export default function HomePage() {
  const { initialized, authenticated, login } = useAuth();

  return (
    <main className="home-public-page">
      <section className="public-hero">
        <div className="public-hero-content">
          <p className="eyebrow">Colegio Bernardo O’Higgins</p>
          <h1>Tradición educativa con gestión digital moderna</h1>
          <p>
            AulaBOH es la plataforma digital del establecimiento para apoyar la gestión
            académica, el registro de clases, la asistencia y el seguimiento del proceso
            educativo.
          </p>

          <div className="hero-actions">
            {authenticated ? (
              <Link className="primary-button" to="/redirigir-rol">
                Ir a mi panel
              </Link>
            ) : (
              <button className="primary-button" type="button" onClick={login} disabled={!initialized}>
                Iniciar sesión
              </button>
            )}
            <Link className="secondary-button" to="/nosotros">
              Conocer el colegio
            </Link>
          </div>
        </div>

        <div className="hero-visual-card">
          <div className="hero-image-frame">
            <span>Imagen institucional del colegio</span>
            <small>Imagen institucional del establecimiento.</small>
          </div>
        </div>
      </section>

      <section className="public-intro">
        <div>
          <p className="eyebrow">Nuestro establecimiento</p>
          <h2>Una comunidad educativa orientada al aprendizaje</h2>
        </div>

        <p>
          El Colegio Bernardo O’Higgins promueve una formación integral, combinando
          desarrollo académico, convivencia escolar y acompañamiento institucional.
          La plataforma AulaBOH permite modernizar procesos antes registrados en libros
          físicos, facilitando una gestión más ordenada y accesible.
        </p>
      </section>

      <section className="public-dashboard">
        <div className="section-heading">
          <p className="eyebrow">Información del colegio</p>
          <h2>Explora nuestras secciones</h2>
          <p>
            Navega por la información pública del establecimiento de forma simple,
            visual e intuitiva.
          </p>
        </div>

        <div className="public-card-grid">
          {publicCards.map((card) => (
            <Link className="public-card" to={card.path} key={card.path}>
              <span className="public-card-icon">{card.icon}</span>
              <h3>{card.title}</h3>
              <p>{card.description}</p>
              <strong>Ver sección →</strong>
            </Link>
          ))}
        </div>
      </section>
    </main>
  );
}
