package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import io.avaje.jsonb.Json;

import java.time.LocalDateTime;

@Json
public record RetrieveAllPaymentsItemResponse(

  String paymentReference,

  String payerReference,

  String payeeReference,

  String paymentAmount,

  String paymentSubject,

  LocalDateTime executionDate,

  String status,

  LocalDateTime createdAt) {

}
