package ch.admin.bit.jeap.jme.bptest.order.testagent.persistence;

import ch.admin.bit.jeap.jme.bptest.order.testagent.domain.TestInstance;
import ch.admin.bit.jeap.jme.bptest.order.testagent.domain.TestInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TestInstanceRepositoryImpl implements TestInstanceRepository {

    private final JpaTestInstanceRepository jpaTestInstanceRepository;

    @Override
    public void addTestCase(TestInstance testInstance) {
        jpaTestInstanceRepository.save(testInstance);

    }

    @Override
    public TestInstance getById(String testId) {
        return jpaTestInstanceRepository.findById(testId).orElseThrow(IllegalStateException::new);
    }

    @Override
    public void updateTestCase(String testId, Map<String, String> data) {
        Optional<TestInstance> testCaseOptional = jpaTestInstanceRepository.findById(testId);
        if (testCaseOptional.isPresent()) {
            TestInstance testInstance = testCaseOptional.get();
            testInstance.setParameters(data);
            jpaTestInstanceRepository.save(testInstance);
        }

    }

}
