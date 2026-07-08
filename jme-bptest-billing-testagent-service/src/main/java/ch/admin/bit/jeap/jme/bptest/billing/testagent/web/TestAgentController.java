package ch.admin.bit.jeap.jme.bptest.billing.testagent.web;

import ch.admin.bit.jeap.jme.bptest.billing.testagent.service.BillingTestAgentService;
import ch.admin.bit.jeap.testagent.api.TestAgentOperations;
import ch.admin.bit.jeap.testagent.api.act.ActionDto;
import ch.admin.bit.jeap.testagent.api.act.ActionResultDto;
import ch.admin.bit.jeap.testagent.api.prepare.PreparationDto;
import ch.admin.bit.jeap.testagent.api.prepare.PreparationResultDto;
import ch.admin.bit.jeap.testagent.api.update.DynamicDataDto;
import ch.admin.bit.jeap.testagent.api.verify.ReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class TestAgentController implements TestAgentOperations {

    private final BillingTestAgentService billingTestAgentService;

    @Override
    @Transactional
    public ResponseEntity<PreparationResultDto> prepare(String testId, PreparationDto preparationDto) {
        switch (preparationDto.getTestCase()) {
            case "OrderBillingHappyPath":
                log.info("PREPARE: Order Billing Happy Path");
                billingTestAgentService.prepareOrderBillingHappyPath(testId, preparationDto);
                return ResponseEntity.ok(PreparationResultDto.builder().build()); //Nothing to Respond
            default:
                throw new IllegalStateException("Initial State not valid");
        }
    }

    @Override
    @Transactional
    public void update(String testId, DynamicDataDto dynamicDataDto) {
        log.info("UPDATE: Update testId {} with {}", testId, dynamicDataDto);
        billingTestAgentService.save(testId, dynamicDataDto);
    }

    @Override
    @Transactional
    public ResponseEntity<ActionResultDto> act(String testId, ActionDto actionDto) {
        // No implementation because no use case
        return ResponseEntity.ok(ActionResultDto.builder().build());
    }

    @Override
    @Transactional
    public ResponseEntity<ReportDto> verify(String testId) {
        ReportDto reportDto = billingTestAgentService.verifyBill(testId);
        log.info("VERIFY: {}", reportDto.toString());
        return ResponseEntity.ok(reportDto);
    }

    @Override
    @Transactional
    public void cleanUp(String testId) {
        billingTestAgentService.cleanUp(testId);
        log.info("CLEAN UP");
    }
}
