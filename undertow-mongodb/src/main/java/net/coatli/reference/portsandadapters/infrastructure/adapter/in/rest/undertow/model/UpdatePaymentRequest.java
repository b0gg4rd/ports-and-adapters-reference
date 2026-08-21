package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import io.avaje.jsonb.Json;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Json
public record UpdatePaymentRequest(

  BigDecimal a1,

  String a2,

  LocalDateTime a3) {

}
