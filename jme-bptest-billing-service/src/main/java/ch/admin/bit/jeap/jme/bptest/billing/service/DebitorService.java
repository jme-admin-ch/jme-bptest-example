package ch.admin.bit.jeap.jme.bptest.billing.service;

import ch.admin.bit.jeap.jme.bptest.billing.api.DebitorNotFoundException;
import ch.admin.bit.jeap.jme.bptest.billing.domain.Debitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class DebitorService {

    private static final Map<String, Debitor> debitors = new HashMap<>();

    public void createDebitor(String debitorId, String customerId) {
        Debitor debitor = new Debitor(debitorId, customerId);
        debitors.put(debitorId, debitor);
        log.info("Debitor {} created successfully", debitor);
    }

    public void deleteDebitor(String debitorId) {
        debitors.remove(debitorId);
        log.info("Debitor with id {} deleted successfully", debitorId);
    }

    public Collection<Debitor> getDebitors() {
        return debitors.values();
    }

    public Debitor findDebitor(String customerId) throws DebitorNotFoundException {
        return debitors.values().stream().filter(d -> d.getCustomerId().equals(customerId)).findFirst().
                orElseThrow(() -> new DebitorNotFoundException(customerId));
    }

}
