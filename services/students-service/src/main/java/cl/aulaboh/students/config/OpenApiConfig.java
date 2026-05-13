package cl.aulaboh.students.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AulaBOH Students Service API",
                version = "1.0.0",
                description = "API REST para administracion de estudiantes.",
                contact = @Contact(name = "AulaBOH Platform Team")
        ),
        servers = @Server(url = "http://localhost:8081", description = "Students Service local")
)
public class OpenApiConfig {
}
