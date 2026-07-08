package ch.admin.bit.jeap.jme.bptest.order.testagent.web;

import ch.admin.bit.jeap.jme.bptest.order.testagent.service.OrderTestAgentService;
import ch.admin.bit.jeap.testagent.api.TestAgentOperations;
import ch.admin.bit.jeap.testagent.api.act.ActionDto;
import ch.admin.bit.jeap.testagent.api.act.ActionResultDto;
import ch.admin.bit.jeap.testagent.api.prepare.PreparationDto;
import ch.admin.bit.jeap.testagent.api.prepare.PreparationResultDto;
import ch.admin.bit.jeap.testagent.api.update.DynamicDataDto;
import ch.admin.bit.jeap.testagent.api.verify.ReportDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
class TestAgentController implements TestAgentOperations {

    private final OrderTestAgentService orderTestAgentService;

    @Override
    public ResponseEntity<PreparationResultDto> prepare(String testId, PreparationDto preparationDto) {
         switch (preparationDto.getTestCase()) {
            case "OrderBillingHappyPath":
                log.info("PREPARE: From Java Orchestrator");
                orderTestAgentService.prepareOrderBillingHappyPath(testId, preparationDto);
                return ResponseEntity.ok(PreparationResultDto.builder().build()); //Nothing to Respond
            default:
                throw new IllegalStateException("Initial State not valid");
        }
    }

    @Override
    @Transactional
    public void update(String testId, DynamicDataDto dynamicDataDto) {
        // No implementation because no use case
    }

    @Override
    @Transactional
    public ResponseEntity<ActionResultDto> act(String testId, ActionDto actionDto) {
        switch (actionDto.getAction()) {
            case "submitOrder":
                log.info("ACT: submitOrder");
                orderTestAgentService.actSubmitOrder(testId);
                return ResponseEntity.ok(ActionResultDto.builder().build()); //Nothing to Respond
            case "somethingElse":
                log.info("ACT: somethingElse");
                throw new IllegalStateException("Act 'somethingElse' not valid");
            default:
                throw new IllegalStateException("Act not valid");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ReportDto> verify(String testId) {
        ReportDto reportDto = orderTestAgentService.verifyOrder(testId);
        log.info("VERIFY: Report {}", reportDto.toString());
        return ResponseEntity.ok(reportDto);
    }

    @Override
    @Transactional
    public void cleanUp(String testId) {
        log.info("CLEAN UP");
        orderTestAgentService.cleanUp(testId);
    }
}
