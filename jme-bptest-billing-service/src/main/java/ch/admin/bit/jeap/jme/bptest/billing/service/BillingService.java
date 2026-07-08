package ch.admin.bit.jeap.jme.bptest.billing.service;

import ch.admin.bit.jeap.jme.bptest.billing.api.Order;
import ch.admin.bit.jeap.jme.bptest.billing.api.OrderItem;
import ch.admin.bit.jeap.jme.bptest.billing.api.ProductNotFoundException;
import ch.admin.bit.jeap.jme.bptest.billing.domain.Bill;
import ch.admin.bit.jeap.jme.bptest.billing.domain.BillItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillingService {

    private static final Map<UUID, Bill> bills = new HashMap<>();

    private final ProductService productService;

    private final OrderClient orderClient;

    public Bill getBill(String billId) {
        return bills.get(UUID.fromString(billId));
    }

    public void deleteBill(String billId) {
        bills.remove(UUID.fromString(billId));
        log.info("Bill with id {} deleted successfully", billId);
    }

    @Async
    public void createBill(String debitorId, Order order) throws ProductNotFoundException {

        try {
            Thread.sleep(Long.parseLong("5000"));
        } catch (InterruptedException e) {
            log.warn("Interrupted!", e);
            //Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        List<BillItem> billItems = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            billItems.add(toBillItem(item));
        }

        Bill bill = new Bill(
                UUID.randomUUID(),
                debitorId,
                order.getOrderId(),
                billItems.stream().mapToInt(BillItem::getPrice).sum(),
                billItems);

        log.info("Bill {} created with totalPrice {}", bill.getBillId(), bill.getTotalPrice());

        bills.put(bill.getBillId(), bill);

        orderClient.orderCharged(bill);

    }

    private BillItem toBillItem(OrderItem orderItem) throws ProductNotFoundException {
        return new BillItem(
                UUID.randomUUID(),
                orderItem.getProductId(),
                computePrice(productService.getPrice(orderItem.getProductId()), orderItem.getQuantity()));
    }

    private int computePrice(int price, int quantity) {
        return price * quantity;
    }

    public Collection<Bill> getBills() {
        return bills.values();
    }

}
