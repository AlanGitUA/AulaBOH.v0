import { Link } from 'react-router-dom';

const basicLevels = [
  {
    name: '1° Básico',
    description:
      'Inicio de la educación básica, con foco en lectoescritura, pensamiento lógico, hábitos escolares y adaptación al entorno educativo.',
  },
  {
    name: '2° Básico',
    description:
      'Fortalecimiento de habilidades iniciales de lectura, escritura, matemática y convivencia escolar.',
  },
  {
    name: '3° Básico',
    description:
      'Desarrollo de mayor autonomía, comprensión lectora, resolución de problemas y participación activa en clases.',
  },
  {
    name: '4° Básico',
    description:
      'Consolidación de aprendizajes fundamentales y fortalecimiento de responsabilidades académicas.',
  },
  {
    name: '5° Básico',
    description:
      'Transición hacia mayores exigencias académicas, incorporando nuevas asignaturas y metodologías de trabajo.',
  },
  {
    name: '6° Básico',
    description:
      'Profundización de contenidos, trabajo colaborativo y preparación para los últimos niveles de enseñanza básica.',
  },
  {
    name: '7° Básico',
    description:
      'Etapa de desarrollo académico y personal, con mayor autonomía y fortalecimiento de habilidades de estudio.',
  },
  {
    name: '8° Básico',
    description:
      'Cierre de la enseñanza básica y preparación para el ingreso a enseñanza media.',
  },
];

const highSchoolLevels = [
  {
    name: '1° Medio',
    description:
      'Inicio del ciclo de enseñanza media, fortaleciendo bases académicas, hábitos de estudio y adaptación al nuevo nivel.',
  },
  {
    name: '2° Medio',
    description:
      'Consolidación de aprendizajes, desarrollo personal y preparación para decisiones académicas posteriores.',
  },
  {
    name: '3° Medio',
    description:
      'Profundización de conocimientos, autonomía y orientación hacia intereses académicos y vocacionales.',
  },
  {
    name: '4° Medio',
    description:
      'Cierre del proceso escolar, preparación para educación superior, mundo laboral y proyectos personales.',
  },
];

function LevelSection({ title, description, levels }) {
  return (
    <section className="levels-block">
      <div className="section-heading">
        <p className="eyebrow">Niveles educativos</p>
        <h2>{title}</h2>
        <p>{description}</p>
      </div>

      <div className="levels-detail-grid">
        {levels.map((level) => (
          <article className="card level-card" key={level.name}>
            <span className="level-badge">{level.name}</span>
            <h3>{level.name}</h3>
            <p>{level.description}</p>
          </article>
        ))}
      </div>
    </section>
  );
}

export default function LevelsPage() {
  return (
    <main className="public-detail-page">
      <section className="public-detail-hero">
        <div>
          <p className="eyebrow">Trayectoria escolar</p>
          <h1>🎓 Enseñanza Básica y Media</h1>
          <p>
            El Colegio Bernardo O’Higgins acompaña el proceso formativo de sus
            estudiantes desde primero básico hasta cuarto medio, promoviendo el
            desarrollo académico, personal y valórico en cada etapa escolar.
          </p>

          <Link className="secondary-button" to="/">
            ← Volver al inicio
          </Link>
        </div>

        <div className="detail-image-frame">
          <span>Imagen de estudiantes, salas de clases o actividades escolares</span>
        </div>
      </section>

      <LevelSection
        title="Enseñanza Básica"
        description="Desde 1° a 8° básico, esta etapa fortalece las bases académicas, hábitos de estudio, convivencia escolar y autonomía progresiva."
        levels={basicLevels}
      />

      <LevelSection
        title="Enseñanza Media"
        description="Desde 1° a 4° medio, esta etapa consolida aprendizajes, orientación vocacional y preparación para nuevos desafíos académicos y personales."
        levels={highSchoolLevels}
      />
    </main>
  );
}