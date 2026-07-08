package ch.admin.bit.jeap.jme.bptest.order.service;

import ch.admin.bit.jeap.jme.bptest.order.api.CustomerNotFoundException;
import ch.admin.bit.jeap.jme.bptest.order.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomerService {

    private static final Map<String, Customer> customers = new HashMap<>();

    public void createCustomer(String customerId, String name) {
        Customer customer = new Customer(customerId, name);
        customers.put(customerId, customer);
        log.info("Customer {} created successfully", customer);
    }

    public void deleteCustomer(String customerId) {
        customers.remove(customerId);
        log.info("Customer with id {} deleted successfully", customerId);
    }

    public Collection<Customer> getCustomers() {
        return customers.values();
    }

    public Customer findCustomer(String customerId) throws CustomerNotFoundException {
        if (customers.containsKey(customerId)) {
            return customers.get(customerId);
        } else {
            throw new CustomerNotFoundException(customerId);
        }

    }

}
