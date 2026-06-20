package net.coatli.reference.portsandadapters.application.port.in.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdatePaymentInput(

  String paymentReference,

  BigDecimal paymentAmount,

  String paymentSubject,

  LocalDateTime executionDate) {

}
