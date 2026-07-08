package ch.admin.bit.jeap.jme.bptest.order.testagent.service;

import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OrderCreationDto {

    String customerId;

    List<OrderCreationItemDto> items;

}
