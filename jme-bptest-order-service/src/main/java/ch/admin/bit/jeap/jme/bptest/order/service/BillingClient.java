package ch.admin.bit.jeap.jme.bptest.order.service;

import ch.admin.bit.jeap.jme.bptest.order.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class BillingClient {

    private final RestClient restClient;
    private final String billingServiceUrl;

    public BillingClient(@Value("${billingService.url}") String billingServiceUrl,
                         RestClient.Builder restClientBuilder) {
        this.billingServiceUrl = billingServiceUrl;
        this.restClient = restClientBuilder.build();
    }

    public void chargeOrder(Order order){
        log.trace("Call the billingService with url {} to create a bill", billingServiceUrl);
        restClient.post()
                .uri(billingServiceUrl + "/api/bill")
                .contentType(MediaType.APPLICATION_JSON)
                .body(order)
                .retrieve()
                .toBodilessEntity();
        log.info("Order successfully sent to billing service");
    }

}
