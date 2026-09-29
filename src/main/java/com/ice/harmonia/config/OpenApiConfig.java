package com.ice.harmonia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Harmonia API")
                        .version("1.0")
                        .description("Microservice endpoints layout handling the Harmonia core backend data processing infrastructure streams.")
                        .contact(new Contact()
                                .name("Amazon Kalimuthu")
                                .email("contactamazon88@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
