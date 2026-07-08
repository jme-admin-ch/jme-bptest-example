package ch.admin.bit.jeap.jme.bptest.billing.domain;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class Bill {

    UUID billId;

    String debitorId;

    UUID orderId;

    int totalPrice;

    List<BillItem> items;

}
