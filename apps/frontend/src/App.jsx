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
import UnauthorizedPage from './pages/UnauthorizedPage';
import ProtectedRoute from './auth/ProtectedRoute';
import RoleRedirect from './auth/RoleRedirect';
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
          <Route path="/redirigir-rol" element={<RoleRedirect />} />

          <Route
            path="/panel"
            element={
              <ProtectedRoute allowedRoles={['ADMIN', 'DOCENTE']}>
                <DashboardPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/estudiantes"
            element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <StudentsPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/gestion-academica"
            element={
              <ProtectedRoute allowedRoles={['ADMIN', 'DOCENTE']}>
                <AcademicPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/resumen-academico"
            element={
              <ProtectedRoute allowedRoles={['ADMIN', 'DOCENTE', 'ESTUDIANTE', 'APODERADO']}>
                <SummaryPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/no-autorizado"
            element={
              <ProtectedRoute>
                <UnauthorizedPage />
              </ProtectedRoute>
            }
          />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
