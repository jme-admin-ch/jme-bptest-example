package ch.admin.bit.jeap.jme.bptest.orchestrator.web;

import ch.admin.bit.jeap.testagent.api.notification.LogDto;
import ch.admin.bit.jeap.testagent.api.notification.NotificationDto;
import ch.admin.bit.jeap.testorchestrator.domain.TestConclusion;
import ch.admin.bit.jeap.testorchestrator.services.LogService;
import ch.admin.bit.jeap.testorchestrator.services.NotificationService;
import ch.admin.bit.jeap.testorchestrator.services.TestCaseService;
import ch.admin.bit.jeap.testorchestrator.services.TestRunService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "TestOrchestrator API")
@RequestMapping(value = "/api/tests")
class TestCaseController {

    private final TestCaseService testCaseService;
    private final LogService logService;
    private final NotificationService notificationService;
    private final TestRunService testRunService;

    @Operation(summary = "Start Test Run and return Test ID. Available TestCases: OrderBillingHappyPath")
    @PostMapping("/{testCase}")
        // This must not be marked as @Transactional. Otherwise the test run will not yet be persisted when test agents
        // invoke the log endpoint during the prepare phase.
    String startTestRun(@PathVariable String testCase) {
        return testCaseService.startTestRun(testCase);
    }

    @Operation(summary = "Log")
    @PostMapping("/{testId}/logs")
    void log(@PathVariable String testId, @RequestBody LogDto logDto) {
        logService.log(testId, logDto);
    }

    @Operation(summary = "Notify")
    @PostMapping("/{testId}/notifications")
    public void notify(@PathVariable String testId, @RequestBody NotificationDto notificationDto) {
        notificationService.notify(testId, notificationDto);
    }

    @Operation(summary = "Get the overall test conclusion of a test run for this testId")
    @GetMapping("/{testId}/conclusion")
    public TestConclusion getOverallTestConclusion(@PathVariable String testId) {
        return testRunService.getOverallTestConclusion(testId);
    }

}
