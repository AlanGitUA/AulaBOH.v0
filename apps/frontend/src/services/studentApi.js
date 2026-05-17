import { request } from './apiClient';

export const studentApi = {
  create: (student) =>
    request('/api/bff/students', {
      method: 'POST',
      body: JSON.stringify(student),
    }),
  update: (studentId, student) =>
    request(`/api/bff/students/${studentId}`, {
      method: 'PUT',
      body: JSON.stringify(student),
    }),
  delete: (studentId) =>
    request(`/api/bff/students/${studentId}`, {
      method: 'DELETE',
    }),
  findAll: () => request('/api/bff/students'),
  summary: (studentId) => request(`/api/bff/students/${studentId}/summary`),
};
