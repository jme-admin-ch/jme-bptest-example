package ch.admin.bit.jeap.jme.bptest.test;

import ch.admin.bit.jeap.jme.test.BootServiceSpringIntegrationTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * End-to-end integration test for the BPTest example. It boots the whole example (two example services,
 * two test agents and the business process test orchestrator) on top of the docker compose infrastructure
 * and then runs the OrderBillingHappyPath business process test through the orchestrator's REST API,
 * just like a user of the example would.
 * <p>
 * The services are started on reserved free ports instead of the fixed ports configured in their
 * {@code application-local.yml}, so the test does not conflict with manually started instances of the
 * services or other processes on the machine. The URLs the services use to call each other are overridden
 * accordingly.
 */
@Slf4j
class BpTestExampleIT extends BootServiceSpringIntegrationTestBase {

    private static final List<Integer> PORTS = reserveFreePorts(5);
    private static final int BILLING_SERVICE_PORT = PORTS.get(0);
    private static final int ORDER_SERVICE_PORT = PORTS.get(1);
    private static final int BILLING_TESTAGENT_PORT = PORTS.get(2);
    private static final int ORDER_TESTAGENT_PORT = PORTS.get(3);
    private static final int ORCHESTRATOR_PORT = PORTS.get(4);

    private static final String BILLING_SERVICE_BASE_URL =
            "http://localhost:" + BILLING_SERVICE_PORT + "/jme-bptest-billing-service";
    private static final String ORDER_SERVICE_BASE_URL =
            "http://localhost:" + ORDER_SERVICE_PORT + "/jme-bptest-order-service";
    private static final String BILLING_TESTAGENT_BASE_URL =
            "http://localhost:" + BILLING_TESTAGENT_PORT + "/jme-bptest-billing-testagent-service";
    private static final String ORDER_TESTAGENT_BASE_URL =
            "http://localhost:" + ORDER_TESTAGENT_PORT + "/jme-bptest-order-testagent-service";
    private static final String ORCHESTRATOR_BASE_URL =
            "http://localhost:" + ORCHESTRATOR_PORT + "/jme-bptest-orchestrator-service";

    @BeforeAll
    static void startServices() throws Exception {
        startService("jme-bptest-billing-service", BILLING_SERVICE_BASE_URL, Map.of(
                "server.port", String.valueOf(BILLING_SERVICE_PORT),
                "orderService.url", ORDER_SERVICE_BASE_URL));
        startService("jme-bptest-order-service", ORDER_SERVICE_BASE_URL, Map.of(
                "server.port", String.valueOf(ORDER_SERVICE_PORT),
                "billingService.url", BILLING_SERVICE_BASE_URL));
        startService("jme-bptest-billing-testagent-service", BILLING_TESTAGENT_BASE_URL, Map.of(
                "server.port", String.valueOf(BILLING_TESTAGENT_PORT),
                "billingService.url", BILLING_SERVICE_BASE_URL));
        startService("jme-bptest-order-testagent-service", ORDER_TESTAGENT_BASE_URL, Map.of(
                "server.port", String.valueOf(ORDER_TESTAGENT_PORT),
                "orderService.url", ORDER_SERVICE_BASE_URL));
        startService("jme-bptest-orchestrator", ORCHESTRATOR_BASE_URL, Map.of(
                "server.port", String.valueOf(ORCHESTRATOR_PORT),
                "orchestrator.callbackUrl", ORCHESTRATOR_BASE_URL,
                // Bracket notation preserves the case of the map keys when binding from system properties
                "orchestrator.testagentURLs[OrderTestAgent]", ORDER_TESTAGENT_BASE_URL,
                "orchestrator.testagentURLs[BillingTestAgent]", BILLING_TESTAGENT_BASE_URL,
                "orchestrator.zephyr.restApiUrl", ORCHESTRATOR_BASE_URL + "/api/zephyr-stub"));
    }

    @Test
    void runOrderBillingHappyPathTestCase() {
        String testId = given()
                .baseUri(ORCHESTRATOR_BASE_URL)
                .when()
                .post("/api/tests/{testCase}", "OrderBillingHappyPath")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        log.info("Started OrderBillingHappyPath test run with testId: {}", testId);

        await().atMost(Duration.ofSeconds(120)).untilAsserted(() -> {
            String conclusion = given()
                    .baseUri(ORCHESTRATOR_BASE_URL)
                    .when()
                    .get("/api/tests/{testId}/conclusion", testId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .get();
            log.info("Test run {} conclusion: {}", testId, conclusion);
            assertThat(conclusion).isEqualTo("PASS");
        });
    }
}
