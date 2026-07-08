package ch.admin.bit.jeap.jme.bptest.order.testagent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrchestratorClient orchestratorClient;
    private final OrderClient orderClient;

    @Async
    public void checkState(String testId, OrderDto orderDto) {

        OrderState orderState = OrderState.OPEN;
        int counter = 0;
        while ((!(orderState==OrderState.CLOSED)) && (counter<10)) {
            orderState = orderClient.getOrderState(orderDto.getOrderId());
            orchestratorClient.logToOrchestrator(testId, "polling...Please be patient");
            log.info("Order State: {}; Attempt: {}", orderState, counter);
            counter++;
            try {
                Thread.sleep(Long.parseLong("3000"));
            } catch (InterruptedException e) {
                log.warn("Interrupted!", e);
                //Restore interrupted state...
                Thread.currentThread().interrupt();
            }
        }
        if (!(orderState==OrderState.CLOSED)) {
            log.info("Too many attemps...No State found. error");
        } else {
            log.info("State is closed...go ahead ");
            orchestratorClient.notifyOrderClosed(testId, orderDto);
        }

    }

}
