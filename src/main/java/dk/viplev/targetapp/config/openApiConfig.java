package dk.viplev.targetapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class openApiConfig {

    private final String appVersion = "1.0.0";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Target App API")
                .description("API documentation for Target App")
                .version(appVersion));
    }
}
