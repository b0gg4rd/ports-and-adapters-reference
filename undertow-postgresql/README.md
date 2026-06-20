# Ports And Adapters Reference - Undertow PostgreSQL

## Overview

Infrastructure module implementing the **inbound** (Undertow REST) and **outbound** (PostgreSQL/MyBatis) adapters for the `ports-and-adapters-reference-core`. It exposes a CRUD API for the `Payment` domain.

| Method   | Path                      | Handler                      |
|----------|---------------------------|------------------------------|
| `GET`    | `/api/v1/ping`            | `ResponseCodeHandler.HANDLE_200` |
| `POST`   | `/api/v1/payments`        | `CreatePaymentHandler`       |
| `GET`    | `/api/v1/payments`        | `RetrieveAllPaymentsHandler` |
| `PUT`    | `/api/v1/payments/{a0}`   | `UpdatePaymentHandler`       |
| `DELETE` | `/api/v1/payments/{a0}`   | `DeletePaymentHandler`       |

---

## Diagrams

### Infrastructure

```mermaid
flowchart LR
  subgraph Infrastructure ["Infrastructure (Adapters)"]
    direction TB
    A_IN[Inbound Adapters\nCreatePaymentHandler\nRetrieveAllPaymentsHandler\nUpdatePaymentHandler\nDeletePaymentHandler]
    A_OUT[Outbound Adapters\nPostgresqlPaymentPersistenceAdapter\nSlf4jLoggingAdapter\nJsoniterJsonTransformationAdapter]
    FW[[Undertow · MyBatis · HikariCP · Jsoniter · Log4j2]]
  end

  subgraph Application ["Application (Ports)"]
    direction TB
    P_IN([Port In\nCreatePaymentPortIn\nRetrieveAllPaymentsPortIn\nUpdatePaymentPortIn\nDeletePaymentPortIn])
    CORE[Core\nPaymentCreator\nPaymentAllRetriever\nPaymentUpdater\nPaymentDeleter]
    P_OUT([Port Out\nPaymentPersistencePortOut\nLoggingPortOut\nJsonTransformationPortOut])
    J_APP[[Java SDK]]
  end

  subgraph Domain ["Domain (Models and Enums)"]
    direction TB
    D_ENT[Models and Enums\nPayment\nPaymentStatus]
    J_DOM[[Java SDK]]
  end

  A_IN -->|Uses| P_IN
  P_IN -->|Implemented by| CORE
  CORE -->|Uses| D_ENT
  CORE -->|Calls| P_OUT
  P_OUT -->|Implemented by| A_OUT

  classDef domain fill:#d4edda,stroke:#28a745,stroke-width:2px,color:#155724;
  classDef app fill:#cce5ff,stroke:#007bff,stroke-width:2px,color:#004085;
  classDef infra fill:#f8d7da,stroke:#dc3545,stroke-width:2px,color:#721c24;
  classDef techLabel fill:#fff,stroke-dasharray: 5 5;

  class Domain,J_DOM domain;
  class Application,P_IN,P_OUT,CORE,J_APP app;
  class Infrastructure,A_IN,A_OUT,FW infra;
  class J_APP,J_DOM,FW techLabel;
```

---

## Structure

```
src/main/java/net/coatli/reference/portsandadapters/
└── infrastructure/
    ├── adapter/
    │   ├── in/
    │   │   └── rest/undertow/
    │   │       ├── CreatePaymentHandler.java
    │   │       ├── RetrieveAllPaymentsHandler.java
    │   │       ├── UpdatePaymentHandler.java
    │   │       ├── DeletePaymentHandler.java
    │   │       ├── config/
    │   │       │   ├── ExceptionConfig.java
    │   │       │   └── RoutesConfig.java
    │   │       └── model/
    │   │           ├── CreatePaymentRequest.java
    │   │           ├── UpdatePaymentRequest.java
    │   │           ├── RetrieveAllPaymentsResponse.java
    │   │           ├── RetrieveAllPaymentsItemResponse.java
    │   │           └── mapper/
    │   │               ├── CreatePaymentHandlerMapper.java
    │   │               ├── RetrieveAllPaymentsHandlerMapper.java
    │   │               ├── UpdatePaymentHandlerMapper.java
    │   │               └── DeletePaymentHandlerMapper.java
    │   └── out/
    │       ├── logging/slf4j/
    │       │   └── Slf4jLoggingAdapter.java
    │       ├── persistence/postgresql/
    │       │   ├── PostgresqlPaymentPersistenceAdapter.java
    │       │   └── mybatis/
    │       │       ├── MyBatisPaymentMapper.java
    │       │       └── model/
    │       │           ├── PaymentRow.java
    │       │           └── mapper/
    │       │               └── PostgresqlPaymentPersistenceMapper.java
    │       └── transformation/jsoniter/
    │           └── JsoniterJsonTransformationAdapter.java
    └── bootstrap/
        ├── ApplicationProperties.java
        ├── PaymentBootstrap.java
        └── di/
            ├── ApplicationCoreModule.java
            ├── InfrastructureAdapterInModule.java
            └── InfrastructureAdapterOutModule.java
```

---

## Build

### Requirements

- [Docker](https://docs.docker.com/engine/install/)

### Run

```shell
docker run \
  --rm \
  -w $(pwd) \
  -v $(pwd)/..:$(pwd)/.. \
  -v $(pwd):$(pwd) \
  -v ${HOME}/.m2:/root/.m2 \
  -v /var/run/docker.sock:/var/run/docker.sock \
  azul/zulu-openjdk-alpine:25.0.3 \
  ./mvnw -Djansi.force=true -ntp -P local -U clean verify
```
