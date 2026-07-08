package ch.admin.bit.jeap.jme.bptest.order.testagent.domain;

import lombok.*;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

/**
 * A TestInstance
 */
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED) // for jpa
@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TestInstance {
    @Id
    @Column
    @NonNull
    @EqualsAndHashCode.Include
    @Getter
    private String testId;

    @NonNull
    @Getter
    private String testCase;

    @NonNull
    @Getter
    private String callbackBaseUrl;

    @Getter
    @NonNull
    private ZonedDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "parameters",
            joinColumns = { @JoinColumn(name = "test_id") })
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @Getter
    @Setter
    private Map<String,String> parameters;

}
