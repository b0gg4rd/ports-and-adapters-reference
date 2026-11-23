package net.coatli.reference.portsandadapters.application.port.in.model;

import java.math.BigDecimal;
import java.time.Instant;

public record CreatePaymentInput(

  String payerReference,

  String payeeReference,

  BigDecimal paymentAmount,

  String paymentSubject,

  Instant executionDate) {

}
