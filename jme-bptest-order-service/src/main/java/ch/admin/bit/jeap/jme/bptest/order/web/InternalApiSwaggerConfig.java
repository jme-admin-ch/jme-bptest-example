package ch.admin.bit.jeap.jme.bptest.order.web;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalApiSwaggerConfig {

    @Bean
    GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("Order API")
                .pathsToMatch("/api/order/**")
                .packagesToScan(this.getClass().getPackageName())
                .build();
    }

    @Bean
    GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("Customer API")
                .pathsToMatch("/api/customer/**")
                .packagesToScan(this.getClass().getPackageName())
                .build();
    }
}
