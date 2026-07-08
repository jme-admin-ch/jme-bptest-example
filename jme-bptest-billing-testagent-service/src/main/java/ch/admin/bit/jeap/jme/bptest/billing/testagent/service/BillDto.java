package ch.admin.bit.jeap.jme.bptest.billing.testagent.service;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BillDto {

    UUID billId;
    String debitorId;
    UUID orderId;
    int totalPrice;
    List<BillItemDto> items;

}
