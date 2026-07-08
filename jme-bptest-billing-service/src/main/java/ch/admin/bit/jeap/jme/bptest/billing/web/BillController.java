package ch.admin.bit.jeap.jme.bptest.billing.web;

import ch.admin.bit.jeap.jme.bptest.billing.api.DebitorNotFoundException;
import ch.admin.bit.jeap.jme.bptest.billing.api.Order;
import ch.admin.bit.jeap.jme.bptest.billing.api.ProductNotFoundException;
import ch.admin.bit.jeap.jme.bptest.billing.domain.Bill;
import ch.admin.bit.jeap.jme.bptest.billing.service.BillingService;
import ch.admin.bit.jeap.jme.bptest.billing.service.DebitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/bill")
class BillController {

    private final DebitorService debitorService;

    private final BillingService billingService;

    @GetMapping(path = "/{billId}")
    public ResponseEntity<Bill> getBill(@PathVariable String billId) {
        Bill bill = billingService.getBill(billId);
        if (bill == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bill);
    }

    @PostMapping
    public void chargeOrder(@RequestBody Order order) throws DebitorNotFoundException, ProductNotFoundException {
        log.info("New order {} to charge. Find Debitor with customerId {}", order.getOrderId(), order.getCustomerId());
        String debitorId = debitorService.findDebitor(order.getCustomerId()).getDebitorId();
        log.info("Debitor {} found. Order accepted.", debitorId);

        billingService.createBill(debitorId, order);
    }

    @DeleteMapping(path = "/{billId}")
    public void deleteBill(@PathVariable String billId) {
        billingService.deleteBill(billId);
    }

    @GetMapping
    public Collection<Bill> getBills(){
        return billingService.getBills();
    }
}
