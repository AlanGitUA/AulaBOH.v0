import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import keycloak, { initializeKeycloak } from './keycloak';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [initialized, setInitialized] = useState(false);
  const [authenticated, setAuthenticated] = useState(false);
  const [tokenParsed, setTokenParsed] = useState(null);

  const refreshAuthState = () => {
    setAuthenticated(Boolean(keycloak.authenticated));
    setTokenParsed(keycloak.tokenParsed || null);
  };

  useEffect(() => {
    let mounted = true;

    initializeKeycloak()
      .then(() => {
        if (!mounted) return;
        refreshAuthState();
        setInitialized(true);
      })
      .catch((error) => {
        console.error('Error inicializando Keycloak:', error);
        if (mounted) {
          setInitialized(true);
        }
      });

    keycloak.onAuthSuccess = refreshAuthState;
    keycloak.onAuthLogout = refreshAuthState;
    keycloak.onTokenExpired = () => {
      keycloak.updateToken(30).then(refreshAuthState).catch(() => logout());
    };

    return () => {
      mounted = false;
    };
  }, []);

  const roles = useMemo(() => {
    return tokenParsed?.realm_access?.roles || [];
  }, [tokenParsed]);

  const hasRole = (role) => roles.includes(role);

  const hasAnyRole = (allowedRoles = []) => {
    if (!allowedRoles.length) return true;
    return allowedRoles.some((role) => roles.includes(role));
  };

  const login = () => {
    keycloak.login({
      redirectUri: `${window.location.origin}/redirigir-rol`,
    });
  };

  const logout = () => {
    keycloak.logout({
      redirectUri: window.location.origin,
    });
  };

  const getToken = async () => {
    if (!keycloak.authenticated) return null;

    try {
      await keycloak.updateToken(30);
      refreshAuthState();
      return keycloak.token;
    } catch (error) {
      console.error('No se pudo actualizar el token:', error);
      logout();
      return null;
    }
  };

  const value = {
    initialized,
    authenticated,
    user: tokenParsed,
    username: tokenParsed?.preferred_username,
    fullName: tokenParsed?.name || tokenParsed?.preferred_username,
    roles,
    hasRole,
    hasAnyRole,
    login,
    logout,
    getToken,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth debe usarse dentro de AuthProvider');
  }

  return context;
}
