package ch.admin.bit.jeap.jme.bptest.order.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class OrderItem {

    UUID itemId;

    String productId;

    int quantity;
}
