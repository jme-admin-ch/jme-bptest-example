package ch.admin.bit.jeap.jme.bptest.orchestrator.testcases;

import ch.admin.bit.jeap.testagent.api.act.ActionDto;
import ch.admin.bit.jeap.testagent.api.act.ActionResultDto;
import ch.admin.bit.jeap.testagent.api.prepare.PreparationDto;
import ch.admin.bit.jeap.testagent.api.prepare.PreparationResultDto;
import ch.admin.bit.jeap.testagent.api.update.DynamicDataDto;
import ch.admin.bit.jeap.testagent.api.verify.ReportDto;
import ch.admin.bit.jeap.testorchestrator.adapter.testagent.TestAgentWebClient;
import ch.admin.bit.jeap.testorchestrator.domain.events.ExecuteDoneEvent;
import ch.admin.bit.jeap.testorchestrator.domain.events.NotificationEvent;
import ch.admin.bit.jeap.testorchestrator.domain.events.TestRunFinishedEvent;
import ch.admin.bit.jeap.testorchestrator.services.TestCaseBaseInterface;
import ch.admin.bit.jeap.testorchestrator.services.TestReportService;
import ch.admin.bit.jeap.testorchestrator.services.TestRunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderBillingHappyPath implements TestCaseBaseInterface {

    final private ApplicationEventPublisher applicationEventPublisher;
    final private TestAgentWebClient testAgentWebClient;
    final private TestReportService testReportService;
    final private TestRunService testRunService;

    final private static String ORDER_TEST_AGENT = "OrderTestAgent";
    final private static String BILLING_TEST_AGENT = "BillingTestAgent";


    @Override
    public String getTestCaseName() {
        return "OrderBillingHappyPath";
    }

    @Override
    public String getJiraProjectKey() {
        return "JEAP";
    }

    @Override
    public String getZephyrTestCaseKey() {
        return "JEAP-T16";
    }

    @Override
    public void prepare(String testId, PreparationDto preparationDto) {
        // The preparationDto is built in the TestCaseService (with the callback Url and the testName)
        // But you can add additional Data like in this example:
        preparationDto.setData(Map.of("demoKey", "demoValue"));
        PreparationResultDto prepResultDtoMonoOrderTestAgent = testAgentWebClient.prepare(ORDER_TEST_AGENT, testId, preparationDto);
        PreparationResultDto prepResultDtoMonoBillingTestAgent = testAgentWebClient.prepare(BILLING_TEST_AGENT, testId, preparationDto);
        //Optional: Do something with the PreparationResultDto
        log.info("Prepare: end");
    }

    @Override
    public void execute(String testId) {
        ActionDto actionDto = ActionDto.builder()
                .action("submitOrder")
                .build();
        ActionResultDto actionResultDto = testAgentWebClient.act(ORDER_TEST_AGENT, testId, actionDto);
        Map<String, String> parameters = testRunService.getParameters(testId);
        //Optional: Do something with the ActionResultDto or the parameters
        log.info("Execute: end");
    }

    @Override
    public void verify(String testId) {
        ReportDto reportDtoBilling = testAgentWebClient.verify(BILLING_TEST_AGENT, testId);
        testReportService.persistTestResult(testId, reportDtoBilling);

        ReportDto reportDtoOrder = testAgentWebClient.verify(ORDER_TEST_AGENT, testId);
        testReportService.persistTestResult(testId, reportDtoOrder);
        log.info("Verify: end");
    }

    @Override
    public void cleanUp(String testId) {
        testAgentWebClient.delete(BILLING_TEST_AGENT, testId);
        testAgentWebClient.delete(ORDER_TEST_AGENT, testId);

        TestRunFinishedEvent testRunFinishedEvent = new TestRunFinishedEvent(this, testId);
        applicationEventPublisher.publishEvent(testRunFinishedEvent);
        log.info("CleanUp: end");
    }

    @Override
    public void onApplicationEvent(NotificationEvent notificationEvent) {
        switch (notificationEvent.getNotification()) {
            case "Order-Created":
                log.info("Order-Created");
                DynamicDataDto dynamicDataDto = new DynamicDataDto(notificationEvent.getData());
                this.testAgentWebClient.update(BILLING_TEST_AGENT, notificationEvent.getTestId(), dynamicDataDto);
                break;
            case "Order-Closed":
                log.info("Order-Closed");
                ExecuteDoneEvent executeDoneEvent = new ExecuteDoneEvent(this,
                        this.getTestCaseName(),
                        notificationEvent.getTestId());
                applicationEventPublisher.publishEvent(executeDoneEvent);
                break;
            default:
                throw new IllegalStateException("Notification '" + notificationEvent.getNotification() + "' not expected");
        }
    }
}
