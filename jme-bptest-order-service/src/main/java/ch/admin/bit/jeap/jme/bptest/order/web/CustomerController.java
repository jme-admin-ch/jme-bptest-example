package ch.admin.bit.jeap.jme.bptest.order.web;

import ch.admin.bit.jeap.jme.bptest.order.domain.Customer;
import ch.admin.bit.jeap.jme.bptest.order.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/customer")
class CustomerController {

    private final CustomerService customerService;

    @PutMapping(path = "/{customerId}")
    public void createCustomer(@PathVariable String customerId, @RequestBody String name) {
        customerService.createCustomer(customerId, name);
    }

    @DeleteMapping(path = "/{customerId}")
    public void deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
    }

    @GetMapping
    public Collection<Customer> getCustomers(){
        return customerService.getCustomers();
    }
}
