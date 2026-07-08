package ch.admin.bit.jeap.jme.bptest.order.testagent.service;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomerDto {
    String customerId;
    String name;
}
