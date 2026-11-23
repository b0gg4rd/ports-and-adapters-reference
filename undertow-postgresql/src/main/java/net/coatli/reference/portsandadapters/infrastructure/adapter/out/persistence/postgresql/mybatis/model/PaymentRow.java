package net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model;

import io.avaje.jsonb.Json;

import java.math.BigDecimal;
import java.time.Instant;

@Json
public record PaymentRow(

  String id,

  String payerId,

  String payeeId,

  BigDecimal amount,

  String subject,

  Instant executionDate,

  String status,

  Instant createdAt) {

}
