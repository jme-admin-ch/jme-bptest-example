package ch.admin.bit.jeap.jme.bptest.billing.api;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String productId) {
        super("No Product found with productId " + productId);
    }
}
