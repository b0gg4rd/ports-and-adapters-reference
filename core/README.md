# Ports And Adapters Reference - Core

## Overview

This project contains the **Application** layer of the **Ports And Adapters** architecture. It was named **core** considering the three blocks: _port in_, _core_, and _port out_.

---

## Structure

```
src/main/java/net/coatli/reference/portsandadapters/
├── domain/
│   ├── enums/
│   │   └── PaymentStatus.java
│   └── model/
│       └── Payment.java
└── application/
    ├── core/
    │   ├── PaymentCreator.java
    │   ├── PaymentAllRetriever.java
    │   ├── PaymentUpdater.java
    │   └── PaymentDeleter.java
    └── port/
        ├── in/
        │   ├── CreatePaymentPortIn.java
        │   ├── RetrieveAllPaymentsPortIn.java
        │   ├── UpdatePaymentPortIn.java
        │   ├── DeletePaymentPortIn.java
        │   └── model/
        │       ├── CreatePaymentInput.java
        │       ├── CreatePaymentOutput.java
        │       ├── RetrieveAllPaymentsInput.java
        │       ├── RetrieveAllPaymentsOutput.java
        │       ├── UpdatePaymentInput.java
        │       ├── UpdatePaymentOutput.java
        │       ├── DeletePaymentInput.java
        │       └── DeletePaymentOutput.java
        └── out/
            └── PaymentPersistencePortOut.java
```

---

## Building

### Requirements

- [Docker](https://docs.docker.com/engine/install/)

### Build

```shell
docker run \
  --rm \
  -w $(pwd) \
  -v $(pwd):$(pwd) \
  -v ${HOME}/.m2:/root/.m2 \
  azul/zulu-openjdk-alpine:25.0.3 \
  ./mvnw -Djansi.force=true -ntp -U clean install
```
