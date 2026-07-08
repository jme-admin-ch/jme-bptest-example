package ch.admin.bit.jeap.jme.bptest.order.web;

import ch.admin.bit.jeap.jme.bptest.order.api.CustomerNotFoundException;
import ch.admin.bit.jeap.jme.bptest.order.api.OrderCreation;
import ch.admin.bit.jeap.jme.bptest.order.api.OrderDto;
import ch.admin.bit.jeap.jme.bptest.order.domain.Order;
import ch.admin.bit.jeap.jme.bptest.order.domain.OrderState;
import ch.admin.bit.jeap.jme.bptest.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/order")
class OrderController {

    private final OrderService orderService;

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping(path = "/{orderId}/state")
    public ResponseEntity<OrderState> getOrderState(@PathVariable String orderId) {
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(order.getState());
    }

    @PostMapping
    public OrderDto submitOrder(@RequestBody OrderCreation orderCreation) throws CustomerNotFoundException {
        OrderDto orderDto = orderService.storeOrder(orderCreation);
        orderService.chargeOrder(orderDto.getOrderId());
        return orderDto;
    }

    @PutMapping(path = "/{orderId}/charged-by")
    public void chargedBy(@PathVariable String orderId, @RequestBody String billId) {
        orderService.chargedBy(orderId, billId);
        orderService.closeOrder(orderId);
    }

    @DeleteMapping(path = "/{orderId}")
    public void deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
    }

    @GetMapping
    public Collection<Order> getOrders(){
        return orderService.getOrders();
    }
}
