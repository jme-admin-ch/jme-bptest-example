package ch.admin.bit.jeap.jme.bptest.billing.api;

public class DebitorNotFoundException extends Exception{

    public DebitorNotFoundException(String customerId) {
        super("No Debitor found with customerId " + customerId);
    }
}
