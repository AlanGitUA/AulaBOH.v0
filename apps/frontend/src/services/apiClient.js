const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {}),
    },
    ...options,
  });

  if (!response.ok) {
    const errorText = await response.text();
    console.error('Error API:', response.status, errorText);
    throw new Error(errorText || 'Error al comunicarse con el servidor');
  }

  if (response.status === 204) return null;
  return response.json();
}
