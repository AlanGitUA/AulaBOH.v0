import { Link, useParams } from 'react-router-dom';
import { institutionalContent } from './institutionalContent';

const keyMap = {
  nosotros: 'nosotros',
  reglamento: 'reglamento',
  'convivencia-escolar': 'convivencia',
  equipo: 'equipo',
  'mision-vision-valores': 'mision',
};

export default function InstitutionalPage() {
  const { section } = useParams();
  const content = institutionalContent[keyMap[section] ?? 'nosotros'];

  return (
    <main className="public-detail-page">
      <section className="public-detail-hero">
        <div>
          <p className="eyebrow">{content.eyebrow}</p>
          <h1>
            <span>{content.icon}</span> {content.title}
          </h1>
          <p>{content.description}</p>

          <Link className="secondary-button" to="/">
            ← Volver al inicio
          </Link>
        </div>

        <div className="detail-image-frame">
          <span>{content.imageLabel}</span>
        </div>
      </section>

      <section className="detail-content-grid">
        {content.highlights.map((item) => (
          <article className="card detail-card" key={item}>
            <span>BOH</span>
            <p>{item}</p>
          </article>
        ))}
      </section>
    </main>
  );
}