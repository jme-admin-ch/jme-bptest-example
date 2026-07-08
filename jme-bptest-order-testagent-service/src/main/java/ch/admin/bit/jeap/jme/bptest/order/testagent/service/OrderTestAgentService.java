package ch.admin.bit.jeap.jme.bptest.order.testagent.service;

import ch.admin.bit.jeap.jme.bptest.order.testagent.domain.TestInstance;
import ch.admin.bit.jeap.jme.bptest.order.testagent.domain.TestInstanceRepository;
import ch.admin.bit.jeap.testagent.api.prepare.PreparationDto;
import ch.admin.bit.jeap.testagent.api.verify.Conclusion;
import ch.admin.bit.jeap.testagent.api.verify.ReportDto;
import ch.admin.bit.jeap.testagent.api.verify.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderTestAgentService {

    private final OrderClient orderClient;
    private final OrchestratorClient orchestratorClient;
    private final OrderService orderService;
    private final TestInstanceRepository testInstanceRepository;

    // Testdata
    private static final String CUSTOMER_1 = "1";
    private static final String PRODUCT_A = "a";
    private static final int QUANTITY_PRODUCT_A = 2;

    //Mapping Name to TestStep
    private static final String VERIFY_ORDER = "0"; // In TestCase JEAP-T16, VerifyOrder ist TestStep #1 (aka Array Position 0)


    public void prepareOrderBillingHappyPath(String testId, PreparationDto preparationDto) {
        this.createTestCase(testId, preparationDto);
        CustomerDto customerDto = CustomerDto.builder().customerId(CUSTOMER_1).name("John Doe").build();
        orderClient.createCustomer(customerDto);
        orchestratorClient.logToOrchestrator(testId, "PREPARE");
    }

    public void actSubmitOrder(String testId) {
        OrderCreationDto orderCreationDto = OrderCreationDto.builder()
                .customerId(CUSTOMER_1)
                .items(List.of(OrderCreationItemDto.builder()
                        .productId(PRODUCT_A)
                        .quantity(QUANTITY_PRODUCT_A)
                        .build()))
                .build();
        OrderDto orderDto = orderClient.submitOrder(orderCreationDto);
        orderService.checkState(testId, orderDto);

        Map<String, String> data = new HashMap<>();
        data.put("orderId", orderDto.getOrderId().toString());


        testInstanceRepository.updateTestCase(testId, data);
        orchestratorClient.notifyOrderCreated(testId, orderDto);
        orchestratorClient.logToOrchestrator(testId, "ACT");

    }

    public ReportDto verifyOrder(String testId) {

        orchestratorClient.logToOrchestrator(testId, "VERIFY");

        TestInstance testInstance = testInstanceRepository.getById(testId);

        OrderDto orderDto = orderClient.getOrder(this.getOrderId(testInstance.getTestId()));

        Conclusion conclusion = Conclusion.FAIL;
        String actualResult = "Order is not closed";
        if (orderDto.getState()==OrderState.CLOSED) {
            conclusion = Conclusion.PASS;
            actualResult = "Order is closed.";
        }
        ResultDto resultDto = ResultDto.builder()
                .name(VERIFY_ORDER)
                .detail(actualResult)
                .conclusion(conclusion)
                .build();

        return ReportDto.builder()
                .testId(testId)
                .testcase(testInstance.getTestCase())
                .results(List.of(resultDto))
                .dateTime(ZonedDateTime.now())
                .build();
    }

    public void cleanUp(String testId) {
        orderClient.deleteCustomer(CUSTOMER_1);
        String orderId = this.getOrderId(testId);
        orderClient.deleteOrder(orderId);
        orchestratorClient.logToOrchestrator(testId, "CLEAN UP");
    }

    private void createTestCase(String testId, PreparationDto preparationDto) {
        Map<String, String> data = preparationDto.getData();

        TestInstance testInstance = TestInstance.builder()
                .testId(testId)
                .testCase(preparationDto.getTestCase())
                .callbackBaseUrl(preparationDto.getCallbackBaseUrl())
                .createdAt(ZonedDateTime.now())
                .parameters(data)
                .build();
        testInstanceRepository.addTestCase(testInstance);
    }

    private String getOrderId(String testId) {
        TestInstance testInstance = testInstanceRepository.getById(testId);
        Map<String, String> parameters = testInstance.getParameters();
        return parameters.get("orderId");
    }
}
