package ch.admin.bit.jeap.jme.bptest.billing.testagent.service;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DebitorDto {

    String debitorId;
    String customerId;

}
