package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import com.jsoniter.annotation.JsonCreator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePaymentRequest(

  String a1,

  String a2,

  BigDecimal a3,

  String a4,

  LocalDateTime a5) {

  @JsonCreator
  public CreatePaymentRequest {
  }

}
