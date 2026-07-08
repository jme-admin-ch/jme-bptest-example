package ch.admin.bit.jeap.jme.bptest.order.testagent.persistence;

import ch.admin.bit.jeap.jme.bptest.order.testagent.domain.TestInstance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaTestInstanceRepository extends CrudRepository<TestInstance, String> {

}
