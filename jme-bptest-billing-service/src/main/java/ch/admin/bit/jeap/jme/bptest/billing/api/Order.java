package ch.admin.bit.jeap.jme.bptest.billing.api;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    UUID orderId;

    String customerId;

    List<OrderItem> items;

}
