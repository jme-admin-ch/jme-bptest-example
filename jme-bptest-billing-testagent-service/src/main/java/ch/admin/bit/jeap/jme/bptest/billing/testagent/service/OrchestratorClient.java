package ch.admin.bit.jeap.jme.bptest.billing.testagent.service;

import ch.admin.bit.jeap.jme.bptest.billing.testagent.domain.TestInstance;
import ch.admin.bit.jeap.jme.bptest.billing.testagent.domain.TestInstanceRepository;
import ch.admin.bit.jeap.testagent.api.notification.LogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class OrchestratorClient {

    private static final String TEST_AGENT = "Billing Testagent";

    private final RestClient restClient;
    private final TestInstanceRepository testInstanceRepository;

    public OrchestratorClient(TestInstanceRepository testInstanceRepository, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
        this.testInstanceRepository = testInstanceRepository;
    }

    public void logToOrchestrator(String testId, String text) {
        String callbackBaseUrl = this.getCallbackBaseUrl(testId);

        LogDto logDto = LogDto.builder()
                .logMessage(text)
                .logLevel(LogLevel.INFO)
                .source(TEST_AGENT)
                .build();

        restClient.post()
                .uri(callbackBaseUrl + "/api/tests/" + testId+ "/logs")
                .contentType(MediaType.APPLICATION_JSON)
                .body(logDto)
                .retrieve()
                .toBodilessEntity();
    }

    private String getCallbackBaseUrl(String testId) {
        TestInstance testInstance = testInstanceRepository.getById(testId);
        return testInstance.getCallbackBaseUrl();
    }
}



