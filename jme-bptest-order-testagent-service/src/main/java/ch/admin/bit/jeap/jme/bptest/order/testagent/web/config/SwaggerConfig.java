package ch.admin.bit.jeap.jme.bptest.order.testagent.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "JME BPTest Order TestAgent Service",
                description = "TestAgent for jme-bptest-order-service"
        ),
        security = {@SecurityRequirement(name = "OIDC Enduser"),
                    @SecurityRequirement(name="OIDC System")}
)
@Configuration
public class SwaggerConfig {
}
