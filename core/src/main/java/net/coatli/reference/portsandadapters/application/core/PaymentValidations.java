package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class PaymentValidations {

  public void requirePositiveAmount(final BigDecimal amount) {

    if (null == amount) {
      throw new PaymentInputException("The value for 'paymentAmount' is required.");
    }

    if (amount.signum() <= 0) {
      throw new PaymentInputException("The value for 'paymentAmount' must be greater than zero.");
    }

  }

  public void requireFutureOrAbsentDate(final LocalDateTime date) {

    if (null != date && date.isBefore(LocalDateTime.now())) {
      throw new PaymentInputException("The value for 'executionDate' must not be a past date.");
    }

  }

  public void requireValidUUID(final String value) {

    if (null == value || value.isBlank()) {
      throw new PaymentInputException("The 'paymentReference' is required and must be a valid UUID.");
    }

    try {
      UUID.fromString(value);
    } catch (IllegalArgumentException e) {
      throw new PaymentInputException("The 'paymentReference' is required and must be a valid UUID.");
    }

  }

}
