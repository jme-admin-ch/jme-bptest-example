package ch.admin.bit.jeap.jme.bptest.orchestrator.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * After each test run the orchestrator reports the test results to a Jira/Zephyr instance
 * (see the {@code orchestrator.zephyr} configuration properties). To keep this example self-contained
 * and runnable without access to a Jira/Zephyr instance, the {@code local} profile points
 * {@code orchestrator.zephyr.restApiUrl} at this stub, which just logs the reported test run.
 */
@RestController
@Slf4j
@Tag(name = "Zephyr Stub API")
@RequestMapping(value = "/api/zephyr-stub")
class ZephyrReportingStubController {

    @Operation(summary = "Accepts and logs a Zephyr test run report instead of forwarding it to a real Zephyr instance")
    @PostMapping("/testrun")
    void acceptTestRunReport(@RequestBody String zephyrTestRunReport) {
        log.info("Received Zephyr test run report (accepted by the local Zephyr stub, not forwarded to a Jira/Zephyr instance): {}",
                zephyrTestRunReport);
    }

}
