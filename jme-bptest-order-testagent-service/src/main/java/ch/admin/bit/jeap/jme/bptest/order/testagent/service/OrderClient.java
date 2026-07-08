package ch.admin.bit.jeap.jme.bptest.order.testagent.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

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

    public void createCustomer(CustomerDto customerDto){
        log.trace("Call the orderService with url {} to create a customerDto", orderServiceUrl);
        restClient.put()
                .uri(orderServiceUrl + "/api/customer/" + customerDto.getCustomerId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerDto)
                .retrieve()
                .toBodilessEntity();
        log.trace("CustomerDto successfully created in order service");
    }

    public OrderDto submitOrder(OrderCreationDto orderCreationDto) {
        log.trace("Call the orderService with url {} to submit a order", orderServiceUrl);
        OrderDto orderDto = restClient.post()
                .uri(orderServiceUrl + "/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderCreationDto)
                .retrieve()
                .body(OrderDto.class);
        log.trace("Order successfully submitted  in order service");
        return orderDto;
    }

    public OrderState getOrderState(UUID orderId) {
        return restClient.get()
                .uri(orderServiceUrl + "/api/order/" + orderId + "/state")
                .retrieve()
                .body(OrderState.class);
    }

    public OrderDto getOrder(String orderId) {
        log.trace("Call the getOrder with url {}", orderServiceUrl);
        return restClient.get()
                .uri(orderServiceUrl + "/api/order/" + orderId)
                .retrieve()
                .body(OrderDto.class);
    }

    public void deleteCustomer(String customerId) {
        restClient.delete()
                .uri(orderServiceUrl + "/api/customer/" + customerId)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteOrder(String orderId) {
        restClient.delete()
                .uri(orderServiceUrl + "/api/order/" + orderId)
                .retrieve()
                .toBodilessEntity();
    }

}
