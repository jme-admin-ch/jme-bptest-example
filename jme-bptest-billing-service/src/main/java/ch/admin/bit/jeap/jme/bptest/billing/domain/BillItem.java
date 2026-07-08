package ch.admin.bit.jeap.jme.bptest.billing.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class BillItem {

    UUID itemId;

    String productId;

    int price;

}
