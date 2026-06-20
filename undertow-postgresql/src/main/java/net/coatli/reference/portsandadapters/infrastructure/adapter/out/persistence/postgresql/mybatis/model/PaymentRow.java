package net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentRow(

  String id,

  String payerId,

  String payeeId,

  BigDecimal amount,

  String subject,

  LocalDateTime executionDate,

  String status,

  LocalDateTime createdAt) {

}
