package ch.admin.bit.jeap.jme.bptest.order.testagent.service;

import ch.admin.bit.jeap.jme.bptest.order.testagent.domain.TestInstance;
import ch.admin.bit.jeap.jme.bptest.order.testagent.domain.TestInstanceRepository;
import ch.admin.bit.jeap.testagent.api.notification.LogDto;
import ch.admin.bit.jeap.testagent.api.notification.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@Slf4j
public class OrchestratorClient {

    private static final String TESTAGENT = "Order Testagent";

    private final RestClient restClient;
    private final TestInstanceRepository testInstanceRepository;

    public OrchestratorClient(TestInstanceRepository testInstanceRepository,
                              RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
        this.testInstanceRepository = testInstanceRepository;
    }

    public void logToOrchestrator(String testId, String text) {
        String callbackBaseUrl = this.getCallbackBaseUrl(testId);

        LogDto logDto = LogDto.builder()
                .logMessage(text)
                .logLevel(LogLevel.INFO)
                .source(TESTAGENT)
                .build();

        restClient.post()
                .uri(callbackBaseUrl + "/api/tests/" + testId+ "/logs")
                .contentType(MediaType.APPLICATION_JSON)
                .body(logDto)
                .retrieve()
                .toBodilessEntity();
    }


    public void notifyOrderCreated(String testId, OrderDto orderDto ) {
        String notification = "Order-Created";
        notifyOrder(testId, orderDto, notification);
    }

    public void notifyOrderClosed(String testId, OrderDto orderDto ) {
        String notification = "Order-Closed";
        notifyOrder(testId, orderDto, notification);
    }

    private void notifyOrder(String testId, OrderDto orderDto, String notification) {
        String callbackBaseUrl = this.getCallbackBaseUrl(testId);
        log.trace("Call the Orchestrator with url {} to notify", callbackBaseUrl);

        NotificationDto notificationDto= NotificationDto.builder()
                .testId(testId)
                .notification(notification)
                .producer("Order TestAgent")
                .data(Map.of("orderId",orderDto.getOrderId().toString()))
                .build();

        restClient.post()
                .uri(callbackBaseUrl + "/api/tests/" + testId + "/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(notificationDto)
                .retrieve()
                .toBodilessEntity();
        log.trace("Orchestrator successfully notified: {}", notification);
    }

    private String getCallbackBaseUrl(String testId) {
        TestInstance testInstance = testInstanceRepository.getById(testId);
        return testInstance.getCallbackBaseUrl();
    }
}



