package ch.admin.bit.jeap.jme.bptest.order.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "JME BPTest Order-Service",
                description = "JME BPTest Order-Service"
        ),
        security = {@SecurityRequirement(name = "OIDC Enduser"), @SecurityRequirement(name="OIDC System")}
)
@Configuration
public class SwaggerConfig {
}
