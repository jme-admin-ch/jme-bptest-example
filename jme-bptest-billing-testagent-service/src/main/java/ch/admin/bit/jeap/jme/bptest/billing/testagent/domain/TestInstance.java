package ch.admin.bit.jeap.jme.bptest.billing.testagent.domain;

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
    @Setter
    @Getter
    private Map<String,String> parameters;




//    @ElementCollection
//    @CollectionTable(name = "order_item_mapping",
//            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")})
//    @MapKeyColumn(name = "item_name")
//    @Column(name = "price")
//    private Map<String, Double> itemPriceMap;

}
