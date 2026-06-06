# Ports And Adapters Reference

## Overview

Reference project for **Ports And Adapters** architecture, implementing the hypothetical **Payment** domain.

---

### Domain

The domain includes the following model and enum:

#### Payment

- Payer Reference
- Payee Reference
- Payment Amount
- Payment Subject
- Execution Date

#### Payment Status

- PENDING
- COMPLETED
- FAILED

---

### Application

The application includes the following ports in:

- CreatePaymentPortIn
- RetrieveAllPaymentsPortIn
- UpdatePaymentPortIn
- DeletePaymentPortIn

---

## Diagrams

```mermaid
flowchart LR
  subgraph Infrastructure ["Infrastructure (Adapters)"]
    direction TB
    A_IN[Adapter In]
    A_OUT[Adapter Out]
    FW[[Frameworks / Libraries]]
  end

  subgraph Application ["Application (Ports)"]
    direction TB
    P_IN([Port In\nCreatePaymentPortIn\nRetrieveAllPaymentsPortIn\nUpdatePaymentPortIn\nDeletePaymentPortIn])
    CORE[Core\nPaymentCreator\nPaymentAllRetriever\nPaymentUpdater\nPaymentDeleter]
    P_OUT([Port Out\nPaymentPersistencePortOut])
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
