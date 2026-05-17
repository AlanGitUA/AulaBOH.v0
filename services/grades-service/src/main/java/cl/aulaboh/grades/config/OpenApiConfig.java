package cl.aulaboh.grades.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AulaBOH Grades Service API",
                version = "1.0.0",
                description = "API REST para evaluaciones y calificaciones.",
                contact = @Contact(name = "AulaBOH Platform Team")
        ),
        servers = @Server(url = "http://localhost:8083", description = "Grades Service local")
)
public class OpenApiConfig {
}
