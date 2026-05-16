import { BrowserRouter, Route, Routes } from 'react-router-dom';
import PublicLayout from './components/PublicLayout';
import AppLayout from './components/AppLayout';
import HomePage from './pages/HomePage';
import InstitutionalPage from './pages/InstitutionalPage';
import LevelsPage from './pages/LevelsPage';
import DashboardPage from './pages/DashboardPage';
import StudentsPage from './pages/StudentsPage';
import AcademicPage from './pages/AcademicPage';
import SummaryPage from './pages/SummaryPage';
import './styles.css';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<PublicLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/:section" element={<InstitutionalPage />} />
          <Route path="/niveles-educativos" element={<LevelsPage />} />
        </Route>

        <Route element={<AppLayout />}>
          <Route path="/panel" element={<DashboardPage />} />
          <Route path="/estudiantes" element={<StudentsPage />} />
          <Route path="/gestion-academica" element={<AcademicPage />} />
          <Route path="/resumen-academico" element={<SummaryPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}