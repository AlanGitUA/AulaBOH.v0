import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL || 'http://localhost:8089',
  realm: import.meta.env.VITE_KEYCLOAK_REALM || 'aulaboh',
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID || 'aulaboh-frontend',
});

let initPromise = null;

export function initializeKeycloak() {
  if (!initPromise) {
    initPromise = keycloak.init({
      onLoad: 'check-sso',
      pkceMethod: 'S256',
      checkLoginIframe: false,
      silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`,
    });
  }

  return initPromise;
}

export default keycloak;
