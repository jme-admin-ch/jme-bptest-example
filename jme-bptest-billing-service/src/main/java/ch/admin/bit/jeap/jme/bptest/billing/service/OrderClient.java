package ch.admin.bit.jeap.jme.bptest.billing.service;


import ch.admin.bit.jeap.jme.bptest.billing.domain.Bill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class OrderClient {

    private final RestClient restClient;
    private final String orderServiceUrl;

    public OrderClient(@Value("${orderService.url}") String orderServiceUrl,
                       RestClient.Builder restClientBuilder) {
        this.orderServiceUrl = orderServiceUrl;
        this.restClient = restClientBuilder.build();
    }

    public void orderCharged(Bill bill){
        log.trace("Call the order with url {} to charged the order", orderServiceUrl);
        restClient.put()
                .uri(orderServiceUrl + "/api/order/" + bill.getOrderId()  +"/charged-by")
                .contentType(MediaType.APPLICATION_JSON)
                .body(bill.getBillId().toString())
                .retrieve()
                .toBodilessEntity();
        log.info("Bill charged on the order service.");
    }

}
