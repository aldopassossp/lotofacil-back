package com.lotofacil.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("API de Análise de Lotofácil")
                        .description("API para análise estatística de sorteios da Lotofácil, importação de resultados e geração de sugestões")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Lotofácil Analyzer")
                                .email("contato@lotofacilanalyzer.com")));
    }
}
