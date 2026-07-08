# JME BPTest Example

This example shows how to test a business process spanning multiple microservices with a jEAP **business process test**
(BP test): A central business test orchestrator runs a test case by coordinating one or more test agents, with each
test agent driving and verifying one of the services participating in the business process.

The example uses the following jEAP libraries:

* [jeap-bptest-orchestrator](https://github.com/jeap-admin-ch/jeap-bptest-orchestrator): Provides the components to set
  up a business process test orchestrator instance (test run lifecycle, test agent coordination, test reporting).
* [jeap-bptestagent-api](https://github.com/jeap-admin-ch/jeap-bptestagent-api): Defines the REST contract
  (`TestAgentOperations`) between the orchestrator and the test agents.

It contains the following modules:

* **jme-bptest-order-service**: An example service under test. Manages customers and orders. Orders are charged
  asynchronously via the billing service.
* **jme-bptest-billing-service**: An example service under test. Manages debitors and creates bills for orders, then
  reports back to the order service.
* **jme-bptest-order-testagent-service**: A test agent for the order service. Prepares test data, submits orders and
  verifies the order state on behalf of the orchestrator.
* **jme-bptest-billing-testagent-service**: A test agent for the billing service. Prepares test data and verifies the
  created bill on behalf of the orchestrator.
* **jme-bptest-orchestrator**: A business process test orchestrator instance with the example test case
  [OrderBillingHappyPath](jme-bptest-orchestrator/src/main/java/ch/admin/bit/jeap/jme/bptest/orchestrator/testcases/OrderBillingHappyPath.java),
  which coordinates the two test agents to test the order/billing business process end-to-end.
* **jme-bptest-test**: End-to-end integration test that starts all services locally and runs the example test case.

## Prerequisites

To use this project, ensure you have the following installed:

1. **Java Development Kit (JDK)**: Version 25.
2. **Docker**: For running the required infrastructure (PostgreSQL databases).

**Note:** Use the provided maven wrapper to build and run the project.

## Getting started

### Infrastructure

Before the services can be started, the infrastructure (three PostgreSQL databases) has to be started using docker:

```shell
docker compose -f docker/docker-compose.yml up
```

### Build

The project itself can be built with a simple

```shell
./mvnw install
```

### Start

Then the individual services can be started using (each in its own terminal):

```shell
./mvnw --projects jme-bptest-billing-service spring-boot:run -Dspring-boot.run.profiles=local
./mvnw --projects jme-bptest-order-service spring-boot:run -Dspring-boot.run.profiles=local
./mvnw --projects jme-bptest-billing-testagent-service spring-boot:run -Dspring-boot.run.profiles=local
./mvnw --projects jme-bptest-order-testagent-service spring-boot:run -Dspring-boot.run.profiles=local
./mvnw --projects jme-bptest-orchestrator spring-boot:run -Dspring-boot.run.profiles=local
```

## Run the business process test

Start a test run for the test case `OrderBillingHappyPath` on the orchestrator:

```shell
curl -X POST "http://localhost:8300/jme-bptest-orchestrator-service/api/tests/OrderBillingHappyPath"
```

The call returns a test id. The orchestrator now runs the test case: it prepares both TestAgents, has the order
TestAgent submit an order, waits for the notifications reporting the progress of the business process, and finally
asks both TestAgents for their verdict (the order TestAgent checks that the order reaches the state `CLOSED`, the
billing TestAgent checks the created bill).

Check the overall test conclusion (`PASS`, `FAIL` or `NO_RESULT`) of the test run:

```shell
curl "http://localhost:8300/jme-bptest-orchestrator-service/api/tests/<testId>/conclusion"
```

The business process needs several seconds to complete, so the conclusion will be `NO_RESULT` until the test run has
finished.

See [OrderBillingHappyPath](jme-bptest-orchestrator/src/main/java/ch/admin/bit/jeap/jme/bptest/orchestrator/testcases/OrderBillingHappyPath.java)
for the test case implementation.
See [OrderBillingHappyPathTest](jme-bptest-orchestrator/src/test/java/ch/admin/bit/jeap/jme/bptest/orchestrator/testcases/OrderBillingHappyPathTest.java)
and [OrderBillingHappyPathAsyncTest](jme-bptest-orchestrator/src/test/java/ch/admin/bit/jeap/jme/bptest/orchestrator/testcases/OrderBillingHappyPathAsyncTest.java)
for test case unit test examples using the test support classes provided by
[jeap-bptest-orchestrator](https://github.com/jeap-admin-ch/jeap-bptest-orchestrator).

**Note:** After each test run the orchestrator reports the test results to a Jira/Zephyr instance. To keep this example
self-contained, the `local` profile points the Zephyr configuration at a stub endpoint on the orchestrator itself that
just logs the report (see
[ZephyrReportingStubController](jme-bptest-orchestrator/src/main/java/ch/admin/bit/jeap/jme/bptest/orchestrator/web/ZephyrReportingStubController.java)).

### Swagger UIs

* Orchestrator: http://localhost:8300/jme-bptest-orchestrator-service/swagger-ui.html
* Order service: http://localhost:8171/jme-bptest-order-service/swagger-ui.html
* Billing service: http://localhost:8170/jme-bptest-billing-service/swagger-ui.html
* Order TestAgent: http://localhost:8271/jme-bptest-order-testagent-service/swagger-ui.html
* Billing TestAgent: http://localhost:8270/jme-bptest-billing-testagent-service/swagger-ui.html

## Try out the example services manually

The order and billing services can also be used directly, without the orchestrator and the TestAgents.

The billing service knows the following products:

| Product | Price |
|---------|-------|
| a       | 10    |
| b       | 15    |
| c       | 3     |

Create a debitor with debitorId=100 for customerId=50 in the **billing service**:

```shell
curl -X PUT "http://localhost:8170/jme-bptest-billing-service/api/debitor/100" -H "Content-Type: application/json" -d "50"
```

Create a customer with customerId=50 and name=hans in the **order service**:

```shell
curl -X PUT "http://localhost:8171/jme-bptest-order-service/api/customer/50" -H "Content-Type: application/json" -d "hans"
```

Create an order for customerId=50 with 2 items in the **order service** (the order is charged asynchronously through
the billing service and closes a few seconds later):

```shell
curl -X POST "http://localhost:8171/jme-bptest-order-service/api/order" -H "Content-Type: application/json" \
  -d '{"customerId":"50","items":[{"productId":"a","quantity":5},{"productId":"b","quantity":3}]}'
```

Check the order state in the **order service** (replace `<orderId>` with the id returned by the previous call):

```shell
curl "http://localhost:8171/jme-bptest-order-service/api/order/<orderId>"
```

Check the created bill in the **billing service**:

```shell
curl "http://localhost:8170/jme-bptest-billing-service/api/bill"
```

## Profiles

* **application-local:** Contains all configurations for running the application locally.
* **application-ci:** Additional configuration overrides for running the integration tests on CI.

## Integration Tests

The `jme-bptest-test` module contains an end-to-end integration test that verifies the whole example: it starts the
docker compose infrastructure, boots all five services and runs the `OrderBillingHappyPath` business process test
through the orchestrator's REST API.

### How it works

The test uses Spring Boot Docker Compose support to automatically start and stop the Docker infrastructure (three
PostgreSQL databases) before and after the test run. It then starts the five Spring Boot services as Maven
subprocesses via `mvnw spring-boot:run` and polls their health endpoints until they are ready. The services are
started on reserved free ports instead of the fixed ports from `application-local.yml` (the URLs the services use to
call each other are overridden accordingly), so the test does not conflict with manually started instances of the
services.

The test itself uses REST-Assured to start the `OrderBillingHappyPath` test case on the orchestrator and Awaitility to
poll the test run conclusion until it is `PASS`.

### Running locally

```shell
# Build and install all local modules
./mvnw install -pl '!:jme-bptest-test'
# Run integration tests
./mvnw verify -pl jme-bptest-test
```

This will:

1. Start the Docker Compose infrastructure (containers are stopped after the test).
2. Build and start the five Spring Boot services on reserved free ports.
3. Run the integration test.
4. Stop all services and containers.

Ensure Docker is running and ports 5432–5434 are available.

### Running on CI

On CI the `CI` environment variable must be set. This activates the `ci` Spring profile which uses
`docker-compose-ci.yml` as an overlay (removing host port bindings and reaching the databases through the container
network). On CI, an isolated Docker network is used to allow for parallel builds.

## Note

This repository is part of the open source distribution of JME. See [github.com/jme-admin-ch/jme](https://github.com/jme-admin-ch/jme)
for more information.

## Changes

This project is versioned using [Semantic Versioning](http://semver.org/) and all changes are documented in
[CHANGELOG.md](./CHANGELOG.md) following the format defined in [Keep a Changelog](http://keepachangelog.com/).

## License

This repository is Open Source Software licensed under the [Apache License 2.0](./LICENSE).
