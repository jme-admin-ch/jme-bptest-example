package ch.admin.bit.jeap.jme.bptest.order.testagent.domain;

import java.util.Map;

public interface TestInstanceRepository {

    void addTestCase(TestInstance testInstance);

    TestInstance getById(String testId);

    void updateTestCase(String testId, Map<String, String> data);

}
