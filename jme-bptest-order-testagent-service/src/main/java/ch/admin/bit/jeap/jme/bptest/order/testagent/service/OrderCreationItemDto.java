package ch.admin.bit.jeap.jme.bptest.order.testagent.service;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OrderCreationItemDto {

    String productId;

    int quantity;
}
