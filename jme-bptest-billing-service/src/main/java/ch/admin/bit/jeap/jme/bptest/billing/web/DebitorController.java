package ch.admin.bit.jeap.jme.bptest.billing.web;

import ch.admin.bit.jeap.jme.bptest.billing.domain.Debitor;
import ch.admin.bit.jeap.jme.bptest.billing.service.DebitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/debitor")
class DebitorController {

    private final DebitorService debitorService;

    @PutMapping(path = "/{debitorId}")
    public void createDebitor(@PathVariable String debitorId, @RequestBody String customerId) {
        debitorService.createDebitor(debitorId, customerId);
    }

    @DeleteMapping(path = "/{debitorId}")
    public void deleteDebitor(@PathVariable String debitorId) {
        debitorService.deleteDebitor(debitorId);
    }

    @GetMapping
    public Collection<Debitor> getDebitors(){
        return debitorService.getDebitors();
    }
}
