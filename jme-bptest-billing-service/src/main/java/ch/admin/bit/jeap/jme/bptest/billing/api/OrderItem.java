package ch.admin.bit.jeap.jme.bptest.billing.api;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    String productId;

    int quantity;
}
