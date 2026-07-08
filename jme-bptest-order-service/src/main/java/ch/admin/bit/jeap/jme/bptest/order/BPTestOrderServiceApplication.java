package ch.admin.bit.jeap.jme.bptest.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Slf4j
@EnableAsync
public class BPTestOrderServiceApplication {
    public static void main(String[] args) {

        Environment env = SpringApplication.run(BPTestOrderServiceApplication.class, args).getEnvironment();

        log.info("\n----------------------------------------------------------\n\t" +
                        "{} is running! \n\t" +
                        "\n" +
                        "\tSwaggerUI: \thttp://localhost:{}{}/swagger-ui.html?urls.primaryName=public-api\n\t" +
                        "Profile(s): \t{}" +
                        "\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path"),
                env.getActiveProfiles());
    }
}
