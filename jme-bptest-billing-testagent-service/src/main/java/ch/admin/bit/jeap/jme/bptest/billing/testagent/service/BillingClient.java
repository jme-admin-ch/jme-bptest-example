package ch.admin.bit.jeap.jme.bptest.billing.testagent.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class BillingClient {

    private final RestClient restClient;
    private final String billingServiceUrl;

    public BillingClient(@Value("${billingService.url}") String billingServiceUrl,
                         RestClient.Builder restClientBuilder) {
        this.billingServiceUrl = billingServiceUrl;
        this.restClient = restClientBuilder.build();
    }

    public void createDebitor(DebitorDto debitorDto) {
        log.trace("Call the billingService with url {} to create a debitorDto", billingServiceUrl);
        restClient.put()
                .uri(billingServiceUrl + "/api/debitor/" + debitorDto.getDebitorId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(debitorDto.getCustomerId())
                .retrieve()
                .toBodilessEntity();
        log.trace("DebitorDto successfully created to billing service");
    }

    public List<BillDto> getBills() {
            return restClient.get()
                    .uri(billingServiceUrl + "/api/bill")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
    }

    public void deleteBill(UUID billId) {
        log.trace("billid: {}", billId.toString());
        restClient.delete()
                .uri(billingServiceUrl + "/api/bill/" + billId)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteDebitor(String debitorId) {
        restClient.delete()
                .uri(billingServiceUrl + "/api/debitor/" + debitorId)
                .retrieve()
                .toBodilessEntity();
    }
}
