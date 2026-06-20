package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import com.jsoniter.annotation.JsonCreator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdatePaymentRequest(

  BigDecimal a1,

  String a2,

  LocalDateTime a3) {

  @JsonCreator
  public UpdatePaymentRequest {
  }

}
