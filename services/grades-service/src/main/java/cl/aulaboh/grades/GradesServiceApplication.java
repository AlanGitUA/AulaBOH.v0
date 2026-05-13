package cl.aulaboh.grades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class GradesServiceApplication {
    public static void main(String[] args) { SpringApplication.run(GradesServiceApplication.class, args); }
    @Bean public RestTemplate restTemplate() { return new RestTemplate(); }
}
