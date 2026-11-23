package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import io.avaje.jsonb.Json;

import java.time.Instant;

@Json
public record RetrieveAllPaymentsItemResponse(

  String paymentReference,

  String payerReference,

  String payeeReference,

  String paymentAmount,

  String paymentSubject,

  Instant executionDate,

  String status,

  Instant createdAt) {

}
