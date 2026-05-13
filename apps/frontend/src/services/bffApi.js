import { request } from './apiClient';

export const bffApi = {
  listStudents: () => request('/api/bff/students'),
  createStudent: (data) =>
    request('/api/bff/students', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
  studentSummary: (studentId) => request(`/api/bff/students/${studentId}/summary`),

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

  createGrade: (data) =>
    request('/api/bff/grades', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
};
