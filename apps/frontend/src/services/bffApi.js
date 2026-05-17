import { request } from './apiClient';

export const bffApi = {
  listStudents: () => request('/api/bff/students'),
  createStudent: (data) =>
    request('/api/bff/students', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
  updateStudent: (studentId, data) =>
    request(`/api/bff/students/${studentId}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),
  deleteStudent: (studentId) =>
    request(`/api/bff/students/${studentId}`, {
      method: 'DELETE',
    }),
  studentSummary: (studentId) => request(`/api/bff/students/${studentId}/summary`),
  ownStudentSummary: () => request('/api/bff/me/summary'),
  myStudents: () => request('/api/bff/me/students'),
  myStudentSummary: (studentId) => request(`/api/bff/me/students/${studentId}/summary`),

  listClasses: () => request('/api/bff/classes'),
  createClass: (data) =>
    request('/api/bff/classes', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  createAttendance: (data) =>
    request('/api/bff/attendances', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  listEvaluations: () => request('/api/bff/evaluations'),
  createEvaluation: (data) =>
    request('/api/bff/evaluations', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
  updateEvaluation: (evaluationId, data) =>
    request(`/api/bff/evaluations/${evaluationId}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  createGrade: (data) =>
    request('/api/bff/grades', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
};
