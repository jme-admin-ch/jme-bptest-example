package ch.admin.bit.jeap.jme.bptest.order.testagent.service;


import lombok.*;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OrderDto {

    OrderState state;

    UUID orderId;

}
