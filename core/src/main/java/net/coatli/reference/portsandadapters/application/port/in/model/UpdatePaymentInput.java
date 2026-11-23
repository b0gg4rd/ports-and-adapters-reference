package net.coatli.reference.portsandadapters.application.port.in.model;

import java.math.BigDecimal;
import java.time.Instant;

public record UpdatePaymentInput(

  String paymentReference,

  BigDecimal paymentAmount,

  String paymentSubject,

  Instant executionDate) {

}
