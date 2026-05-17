import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import AcademicForms from '../components/AcademicForms';
import { bffApi } from '../services/bffApi';

export default function AcademicPage() {
  const [students, setStudents] = useState([]);
  const [classes, setClasses] = useState([]);
  const [evaluations, setEvaluations] = useState([]);

  const safeLoad = async (loader, fallback) => {
    try {
      return await loader();
    } catch (error) {
      console.error(error);
      return fallback;
    }
  };

  const loadData = async () => {
    const [studentsData, classesData, evaluationsData] = await Promise.all([
      safeLoad(bffApi.listStudents, []),
      safeLoad(bffApi.listClasses, []),
      safeLoad(bffApi.listEvaluations, []),
    ]);

    setStudents(studentsData ?? []);
    setClasses(classesData ?? []);
    setEvaluations(evaluationsData ?? []);
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <main className="page">
      <div className="page-header">
        <div>
          <p className="eyebrow">Módulo académico</p>
          <h2>Gestión académica</h2>
          <p>
            Registra clases, asistencia, evaluaciones y calificaciones desde el frontend usando el BFF.
          </p>
        </div>

        <Link to="/" className="secondary-button">
          ← Volver al inicio
        </Link>
      </div>

      <AcademicForms
        students={students}
        classes={classes}
        evaluations={evaluations}
        onChanged={loadData}
      />
    </main>
  );
}