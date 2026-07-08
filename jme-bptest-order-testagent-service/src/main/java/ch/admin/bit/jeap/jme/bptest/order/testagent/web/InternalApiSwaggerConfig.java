package ch.admin.bit.jeap.jme.bptest.order.testagent.web;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalApiSwaggerConfig {

    @Bean
    GroupedOpenApi testAgentApi() {
        return GroupedOpenApi.builder()
                .group("Testagent API")
                .pathsToMatch("/api/tests/**")
                .packagesToScan(this.getClass().getPackageName())
                .build();
    }


}
