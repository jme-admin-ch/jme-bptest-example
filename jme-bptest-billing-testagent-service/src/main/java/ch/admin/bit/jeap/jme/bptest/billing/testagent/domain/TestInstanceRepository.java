package ch.admin.bit.jeap.jme.bptest.billing.testagent.domain;


import java.util.Map;

public interface TestInstanceRepository {

    void addTestCase(TestInstance testInstance);

    void updateTestCase(String testId, Map<String, String> data);

    TestInstance getById(String testId);

}
