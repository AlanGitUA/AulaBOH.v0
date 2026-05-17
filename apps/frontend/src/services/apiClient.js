import keycloak from '../auth/keycloak';

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function buildHeaders(options) {
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers ?? {}),
  };

  if (keycloak.authenticated) {
    try {
      await keycloak.updateToken(30);
      headers.Authorization = `Bearer ${keycloak.token}`;
    } catch (error) {
      console.error('No se pudo actualizar el token de Keycloak:', error);
    }
  }

  return headers;
}

export async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: await buildHeaders(options),
  });

  if (!response.ok) {
    const errorText = await response.text();
    console.error('Error API:', response.status, errorText);
    throw new Error(errorText || 'Error al comunicarse con el servidor');
  }

  if (response.status === 204) return null;
  return response.json();
}
