package ch.admin.bit.jeap.jme.bptest.billing.service;

import ch.admin.bit.jeap.jme.bptest.billing.api.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ProductService {

    private static final Map<String, Integer> productList = new HashMap<>();

    static {
        productList.put("a", 10);
        productList.put("b", 15);
        productList.put("c", 3);
    }

    public Integer getPrice(String productId) throws ProductNotFoundException {
        if (productList.containsKey(productId)) {
            return productList.get(productId);
        } else {
            throw new ProductNotFoundException(productId);
        }
    }

}
