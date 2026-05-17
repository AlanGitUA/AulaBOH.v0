package cl.aulaboh.bff.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AulaBOH BFF API",
                version = "1.0.0",
                description = "API orientada al frontend para orquestar estudiantes, asistencia y calificaciones.",
                contact = @Contact(name = "AulaBOH Platform Team")
        ),
        servers = @Server(url = "http://localhost:8080", description = "BFF local")
)
public class OpenApiConfig {
}
