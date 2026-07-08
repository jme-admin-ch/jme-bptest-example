package ch.admin.bit.jeap.jme.bptest.billing.web;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalApiSwaggerConfig {

    @Bean
    GroupedOpenApi billApi() {
        return GroupedOpenApi.builder()
                .group("Bill API")
                .pathsToMatch("/api/bill/**")
                .packagesToScan(this.getClass().getPackageName())
                .build();
    }

    @Bean
    GroupedOpenApi debitorApi() {
        return GroupedOpenApi.builder()
                .group("Debitor API")
                .pathsToMatch("/api/debitor/**")
                .packagesToScan(this.getClass().getPackageName())
                .build();
    }
}
