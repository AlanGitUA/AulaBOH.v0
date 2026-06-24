package cl.aulaboh.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@Profile("!test")
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/bff/me/**").hasAnyRole("ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO")
                        .requestMatchers(HttpMethod.POST, "/api/bff/students", "/api/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bff/students/**", "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bff/students/**", "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/bff/students", "/api/students").hasAnyRole("ADMIN", "DOCENTE")
                        .requestMatchers(
                                "/api/bff/students/**",
                                "/api/bff/classes/**",
                                "/api/bff/attendances/**",
                                "/api/bff/evaluations/**",
                                "/api/bff/grades/**",
                                "/api/students/**"
                        ).hasAnyRole("ADMIN", "DOCENTE")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(realmRoleConverter());
        return converter;
    }

    private Converter<Jwt, Collection<GrantedAuthority>> realmRoleConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();

        return jwt -> {
            Collection<GrantedAuthority> scopeAuthorities = scopeConverter.convert(jwt);
            Set<GrantedAuthority> authorities = new LinkedHashSet<>();

            if (scopeAuthorities != null) {
                authorities.addAll(scopeAuthorities);
            }

            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

            if (realmAccess == null) {
                return authorities;
            }

            Object roles = realmAccess.get("roles");

            if (roles instanceof Collection<?> realmRoles) {
                realmRoles.stream()
                        .map(String::valueOf)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .forEach(authorities::add);
            }

            return authorities;
        };
    }
}