package ch.admin.bit.jeap.jme.bptest.order.api;

public class CustomerNotFoundException extends Exception{

    public CustomerNotFoundException(String customerId) {
        super("No Customer found with customerId " + customerId);
    }
}
