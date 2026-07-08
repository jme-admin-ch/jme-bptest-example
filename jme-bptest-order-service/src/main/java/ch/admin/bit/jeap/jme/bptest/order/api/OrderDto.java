package ch.admin.bit.jeap.jme.bptest.order.api;

import ch.admin.bit.jeap.jme.bptest.order.domain.OrderState;
import lombok.Value;

import java.util.UUID;

@Value
public class OrderDto {

    OrderState state;

    UUID orderId;

}
