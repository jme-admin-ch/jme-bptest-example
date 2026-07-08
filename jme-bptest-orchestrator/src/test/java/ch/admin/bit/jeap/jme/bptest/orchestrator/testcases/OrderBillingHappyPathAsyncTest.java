package ch.admin.bit.jeap.jme.bptest.orchestrator.testcases;

import ch.admin.bit.jeap.testagent.api.act.ActionDto;
import ch.admin.bit.jeap.testagent.api.notification.NotificationDto;
import ch.admin.bit.jeap.testagent.api.update.DynamicDataDto;
import ch.admin.bit.jeap.testorchestrator.services.TestRunService;
import ch.admin.bit.jeap.testorchestrator.testsupport.TestCaseTestBase;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Same as {@link OrderBillingHappyPathTest} but using asynchronous notifications, which
 * in case of the OrderBillingHappyPath would not be necessary.
 */
public class OrderBillingHappyPathAsyncTest extends TestCaseTestBase {

    private static final String ORDER_TEST_AGENT_NAME = "OrderTestAgent";
    private static final String BILLING_TEST_AGENT_NAME = "BillingTestAgent";

    private static final String ACTION_SUBMIT_ORDER = "submitOrder";
    private static final String NOTIFICATION_ORDER_CREATED = "Order-Created";
    private static final String NOTIFICATION_ORDER_CLOSED = "Order-Closed";

    private static final String ORDER_ID = "123456";
    private static final String ORDER_ID_DATA_NAME = "orderId";

    @Mock
    TestRunService testRunService;

    @Test
    void testOrderBillingHappyPathAsync() throws Exception {

        // Create test case
        OrderBillingHappyPath testCase = new OrderBillingHappyPath(testcaseRunner.getApplicationEventPublisher(), testAgentWebClientMock, testReportServiceMock, testRunService);

        // mock 'act' on order test agent
        testCaseMockTool.mockActCall(ORDER_TEST_AGENT_NAME, () -> {
            NotificationDto orderCreatedNotificationDto = createOrderCreatedNotification(testId);
            testcaseRunner.notifyAsync(orderCreatedNotificationDto, 2, TimeUnit.SECONDS);
            NotificationDto orderClosedNotificationDto = createOrderClosedNotification(testId);
            testcaseRunner.notifyAsync(orderClosedNotificationDto, 5, TimeUnit.SECONDS);
            return null;  // no result to provide to the test case
        });

        // run test case with asynchronous notifications, expect it to not take longer than 10 seconds.
        testcaseRunner.runAsync(testCase).get(10, TimeUnit.SECONDS);

        // assert prepare phase calls
        testCaseMockTool.assertPrepareCalled(ORDER_TEST_AGENT_NAME, testCase.getTestCaseName());
        testCaseMockTool.assertPrepareCalled(BILLING_TEST_AGENT_NAME, testCase.getTestCaseName());

        // assert act phase calls
        ActionDto submitOrderActionDto = testCaseMockTool.assertActCalled(ORDER_TEST_AGENT_NAME, ACTION_SUBMIT_ORDER);
        assertThat(submitOrderActionDto.getData()).isNullOrEmpty();
        DynamicDataDto billingDynamicDataDto = testCaseMockTool.assertUpdateCalled(BILLING_TEST_AGENT_NAME);
        assertThat(billingDynamicDataDto.getData().get(ORDER_ID_DATA_NAME)).isEqualTo(ORDER_ID);

        // assert verify phase calls
        testCaseMockTool.assertVerifyCalled(ORDER_TEST_AGENT_NAME, BILLING_TEST_AGENT_NAME);
        testCaseMockTool.assertPersistTestResultCalled(2);
        testCaseMockTool.assertNoMoreTestReportServiceInteractions();

        // assert cleanup phase calls
        testCaseMockTool.assertDeleteCalled(ORDER_TEST_AGENT_NAME, BILLING_TEST_AGENT_NAME);

        // assert that there were no other test agent calls
        testCaseMockTool.assertNoMoreTestAgentWebClientInteractions();

        // assert test case run has finished
        assertThat(testcaseRunner.hasFinished()).isTrue();
    }

    private NotificationDto createOrderCreatedNotification(String testId) {
        return NotificationDto.builder()
                .testId(testId)
                .notification(NOTIFICATION_ORDER_CREATED)
                .producer(ORDER_TEST_AGENT_NAME)
                .data(Map.of(ORDER_ID_DATA_NAME, ORDER_ID))
                .build();
    }

    private NotificationDto createOrderClosedNotification(String testId) {
        return NotificationDto.builder()
                .testId(testId)
                .notification(NOTIFICATION_ORDER_CLOSED)
                .producer(ORDER_TEST_AGENT_NAME)
                .data(Map.of(ORDER_ID_DATA_NAME, ORDER_ID))
                .build();
    }

}
