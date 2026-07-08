package ch.admin.bit.jeap.jme.bptest.order.api;

import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreation {

    String customerId;

    List<OrderCreationItem> items;

}
