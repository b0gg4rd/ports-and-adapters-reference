package net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb.model;

import io.avaje.jsonb.Json;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

@Json
public record PaymentDocument(

  @BsonId
  String id,

  @BsonProperty("payer_id")
  String payerId,

  @BsonProperty("payee_id")
  String payeeId,

  @BsonProperty("amount")
  BigDecimal amount,

  @BsonProperty("subject")
  String subject,

  @BsonProperty("execution_date")
  Instant executionDate,

  @BsonProperty("status")
  String status,

  @BsonProperty("created_at")
  Instant createdAt) {

}
