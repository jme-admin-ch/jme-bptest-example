package ch.admin.bit.jeap.jme.bptest.billing.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "JME BPTest Billing-Service",
                description = "JME BPTest Billing-Service"
        ),
        security = {@SecurityRequirement(name = "OIDC Enduser"), @SecurityRequirement(name="OIDC System")}
)
@Configuration
public class SwaggerConfig {
}
