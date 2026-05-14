import { BrowserRouter, Route, Routes } from 'react-router-dom';
import AppLayout from './components/AppLayout';
import DashboardPage from './pages/DashboardPage';
import StudentsPage from './pages/StudentsPage';
import AcademicPage from './pages/AcademicPage';
import SummaryPage from './pages/SummaryPage';
import './styles.css';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AppLayout />}>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/estudiantes" element={<StudentsPage />} />
          <Route path="/gestion-academica" element={<AcademicPage />} />
          <Route path="/resumen-academico" element={<SummaryPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}