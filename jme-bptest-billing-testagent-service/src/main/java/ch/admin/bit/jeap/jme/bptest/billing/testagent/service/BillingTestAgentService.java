package ch.admin.bit.jeap.jme.bptest.billing.testagent.service;

import ch.admin.bit.jeap.jme.bptest.billing.testagent.domain.TestInstance;
import ch.admin.bit.jeap.jme.bptest.billing.testagent.domain.TestInstanceRepository;
import ch.admin.bit.jeap.testagent.api.prepare.PreparationDto;
import ch.admin.bit.jeap.testagent.api.update.DynamicDataDto;
import ch.admin.bit.jeap.testagent.api.verify.Conclusion;
import ch.admin.bit.jeap.testagent.api.verify.ReportDto;
import ch.admin.bit.jeap.testagent.api.verify.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillingTestAgentService {

    private final BillingClient billingClient;
    private final TestInstanceRepository testInstanceRepository;
    private final OrchestratorClient orchestratorClient;

    // Testdata
    private static final String CUSTOMER_1 = "1";
    private static final String DEBITOR_1 = "100";

    //Mapping Name to TestStep
    private static final String VERIFY_BILLING = "1"; // In TestCase JEAP-T16, BillingOrder ist TestStep #2 (aka Array Position 1)
    private static final String VERIFY_BILLING_2 = "2"; // In TestCase JEAP-T16, BillingOrder ist TestStep #3 (aka Array Position 2)

    //Expected PRICE for ORDER
    private static final int EXPECTED_PRICE = 20;

    public void prepareOrderBillingHappyPath(String testId, PreparationDto preparationDto) {
        this.createTestCase(testId, preparationDto);

        DebitorDto debitorDto = DebitorDto.builder()
                .debitorId(DEBITOR_1)
                .customerId(CUSTOMER_1)
                .build();
        billingClient.createDebitor(debitorDto);
        orchestratorClient.logToOrchestrator(testId, "PREPARE");

    }

    public void save(String testId, DynamicDataDto dynamicDataDto) {
        testInstanceRepository.updateTestCase(testId, dynamicDataDto.getData());
        orchestratorClient.logToOrchestrator(testId, "UPDATE");
    }

    public ReportDto verifyBill(String testId) {
        orchestratorClient.logToOrchestrator(testId, "VERIFY");
        TestInstance testInstance = testInstanceRepository.getById(testId);
        Map<String, String> parameters = testInstance.getParameters();
        String orderId = parameters.get("orderId");

        BillDto bill = billingClient.getBills().stream()
                .filter(billDto -> billDto.getOrderId().toString().equals(orderId))
                .findFirst()
                .orElse(null);

        ResultDto resultDto;
        if (bill==null) {
                resultDto = ResultDto.builder()
                        .name(VERIFY_BILLING)
                        .detail("Bill is null")
                        .conclusion(Conclusion.FAIL)
                        .build();
        } else if (bill.totalPrice==EXPECTED_PRICE) {
            resultDto = ResultDto.builder()
                    .name(VERIFY_BILLING)
                    .detail("Bill is " + bill.totalPrice + " as expected.")
                    .conclusion(Conclusion.PASS)
                    .build();
        } else {
            resultDto = ResultDto.builder()
                    .name(VERIFY_BILLING)
                    .detail("Bill is " + bill.totalPrice + ". Expected is " + EXPECTED_PRICE + ".")
                    .conclusion(Conclusion.FAIL)
                    .build();
        }

        // Dummy Result to illustrate mulitple Results
        ResultDto secondResultDto = ResultDto.builder()
                .name(VERIFY_BILLING_2)
                .detail("This is a dummy Result to illustrate a second Verification")
                .conclusion(Conclusion.PASS)
                .build();

        return ReportDto.builder()
                .testId(testId)
                .testcase(testInstance.getTestCase())
                .results(List.of(resultDto, secondResultDto))
                .dateTime(ZonedDateTime.now())
                .build();
    }

    public void cleanUp(String testId) {
        billingClient.deleteDebitor(DEBITOR_1);
        String orderId = this.getOrderId(testId);
        billingClient.getBills().stream()
                .filter(billDto -> billDto.getOrderId().toString().equals(orderId))
                .findFirst()
                .ifPresent( bill -> billingClient.deleteBill(bill.getBillId()));
        orchestratorClient.logToOrchestrator(testId, "CLEAN UP");
    }

    private void createTestCase(String testId, PreparationDto preparationDto) {
        TestInstance testInstance = TestInstance.builder()
                .testId(testId)
                .testCase(preparationDto.getTestCase())
                .callbackBaseUrl(preparationDto.getCallbackBaseUrl())
                .createdAt(ZonedDateTime.now())
                .parameters(preparationDto.getData())
                .build();
        testInstanceRepository.addTestCase(testInstance);
    }

    private String getOrderId(String testId) {
        TestInstance testInstance = testInstanceRepository.getById(testId);
        Map<String, String> parameters = testInstance.getParameters();
        return parameters.get("orderId");
    }
}
