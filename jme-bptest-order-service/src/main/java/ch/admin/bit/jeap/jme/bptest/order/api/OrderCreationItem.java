package ch.admin.bit.jeap.jme.bptest.order.api;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreationItem {

    String productId;

    int quantity;
}
