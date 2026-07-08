package ch.admin.bit.jeap.jme.bptest.order.service;

import ch.admin.bit.jeap.jme.bptest.order.api.CustomerNotFoundException;
import ch.admin.bit.jeap.jme.bptest.order.api.OrderCreation;
import ch.admin.bit.jeap.jme.bptest.order.api.OrderCreationItem;
import ch.admin.bit.jeap.jme.bptest.order.api.OrderDto;
import ch.admin.bit.jeap.jme.bptest.order.domain.Order;
import ch.admin.bit.jeap.jme.bptest.order.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private static final Map<UUID, Order> orders = new HashMap<>();

    private final CustomerService customerService;

    private final BillingClient billingClient;

    public void deleteOrder(String orderId) {
        orders.remove(UUID.fromString(orderId));
        log.info("Order with id {} deleted successfully", orderId);
    }

    public OrderDto storeOrder(OrderCreation orderCreation) throws CustomerNotFoundException {
        Order order = new Order(
                UUID.randomUUID(),
                customerService.findCustomer(orderCreation.getCustomerId()).getCustomerId(),
                orderCreation.getItems().stream().map(OrderService::toOrderItem).collect(Collectors.toList()));

        orders.put(order.getOrderId(), order);

        return new OrderDto(order.getState(), order.getOrderId());
    }

    @Async
    public void chargeOrder(UUID orderId) {
        try {
            Thread.sleep(Long.parseLong("5000"));
        } catch (InterruptedException e) {
            log.warn("Interrupted!", e);
            //Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        Order order = orders.get(orderId);
        billingClient.chargeOrder(order);
        order.accept();
    }


    public Order getOrder(String orderId) {
        return orders.get(UUID.fromString(orderId));
    }

    public Collection<Order> getOrders() {
        return orders.values();
    }

    private static OrderItem toOrderItem(OrderCreationItem orderCreationItem) {
        return new OrderItem(UUID.randomUUID(), orderCreationItem.getProductId(), orderCreationItem.getQuantity());
    }

    public void chargedBy(String orderId, String billId) {
        Order order = orders.get(UUID.fromString(orderId));
        order.chargedByBill(UUID.fromString(billId));
    }

    @Async
    public void closeOrder(String orderId) {

        try {
            Thread.sleep(Long.parseLong("5000"));
        } catch (InterruptedException e) {
            log.warn("Interrupted!", e);
            //Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        Order order = orders.get(UUID.fromString(orderId));
        order.close();
    }
}
