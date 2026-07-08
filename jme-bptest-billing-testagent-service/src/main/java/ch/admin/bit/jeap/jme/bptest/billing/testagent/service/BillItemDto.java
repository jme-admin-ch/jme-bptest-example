package ch.admin.bit.jeap.jme.bptest.billing.testagent.service;

import lombok.*;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BillItemDto {

    UUID itemId;
    String productId;
    int price;

}
