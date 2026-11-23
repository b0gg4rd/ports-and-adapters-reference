package net.coatli.reference.portsandadapters.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.coatli.reference.portsandadapters.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Accessors(
  chain = true,
  fluent = false)
@NoArgsConstructor
public class Payment {

  /**
   * Identifier of the payment.
   */
  private String paymentReference;

  /**
   * Identifier of the payer.
   */
  private String payerReference;

  /**
   * Identifier of the payee.
   */
  private String payeeReference;

  /**
   * Amount of the payment.
   */
  private BigDecimal paymentAmount;

  /**
   * Subject of the payment.
   */
  private String paymentSubject;

  /**
   * Date of execution.
   */
  private Instant executionDate;

  /**
   * Status of the payment.
   */
  private PaymentStatus status;

  /**
   * Date time of the creation.
   */
  private Instant createdAt;

}
