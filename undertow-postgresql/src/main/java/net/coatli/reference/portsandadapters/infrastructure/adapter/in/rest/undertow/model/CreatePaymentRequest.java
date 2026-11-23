package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import io.avaje.jsonb.Json;

import java.math.BigDecimal;
import java.time.Instant;

@Json
public record CreatePaymentRequest(

  String payerReference,

  String payeeReference,

  BigDecimal paymentAmount,

  String paymentSubject,

  Instant executionDate) {

}
