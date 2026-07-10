package ec.edu.solca.repositorio;

import ec.edu.solca.repositorio.security.JwtService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class RepositorioClinicoRegionalApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepositorioClinicoRegionalApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder, JwtService jwtService) {
        return builder
                .additionalInterceptors((request, body, execution) -> {
                    request.getHeaders().set(HttpHeaders.AUTHORIZATION,
                            "Bearer " + jwtService.generateToken("repositorio-service", "ADMIN"));
                    return execution.execute(request, body);
                })
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }
}
