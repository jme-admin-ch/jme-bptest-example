package ch.admin.bit.jeap.jme.bptest.order.testagent.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("ch.admin.bit.jeap.jme.bptest.order.testagent.persistence")
@EntityScan(basePackages = "ch.admin.bit.jeap.jme.bptest.order.testagent.domain")
@ComponentScan
class PersistenceConfiguration {


}
