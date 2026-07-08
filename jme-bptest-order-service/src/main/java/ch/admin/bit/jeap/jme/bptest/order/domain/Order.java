package ch.admin.bit.jeap.jme.bptest.order.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Slf4j
public class Order {

    private final UUID orderId;

    private OrderState state;

    private final String customerId;

    private final List<OrderItem> items;

    private UUID billId;

    public Order(UUID orderId, String customerId, List<OrderItem> items) {
        this.orderId = orderId;
        this.state = OrderState.OPEN;
        this.customerId = customerId;
        this.items = items;
        log.info("Order {} created", this.orderId);
    }

    public void chargedByBill(UUID billId) {
        this.billId = billId;
        this.state = OrderState.CHARGED;
        log.info("Order {} charged with billId {} ", this.orderId, billId);
    }

    public void accept() {
        this.state = OrderState.ACCEPTED;
        log.info("Order {} accepted", this.orderId);
    }

    public void close() {
        this.state = OrderState.CLOSED;
        log.info("Order {} closed", this.orderId);
    }
}
