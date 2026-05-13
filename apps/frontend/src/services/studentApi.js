import { request } from './apiClient';

export const studentApi = {
  create: (student) =>
    request('/api/bff/students', {
      method: 'POST',
      body: JSON.stringify(student),
    }),
  findAll: () => request('/api/bff/students'),
  summary: (studentId) => request(`/api/bff/students/${studentId}/summary`),
};
