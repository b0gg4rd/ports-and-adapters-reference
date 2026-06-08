package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class PaymentValidations {

  public void requirePositiveAmount(BigDecimal amount) {

    Optional
      .ofNullable(amount)
      .map(value -> {
        if (value.signum() <= 0) {
          throw new PaymentInputException("The value for 'paymentAmount' must be greater than zero.");
        }
        return value;
      })
      .orElseThrow(() -> new PaymentInputException("The value for 'paymentAmount' is required."));

  }

  public void requireFutureOrAbsentDate(LocalDateTime date) {

    Optional
      .ofNullable(date)
      .ifPresent(d -> {
        if (d.isBefore(LocalDateTime.now())) {
          throw new PaymentInputException("The value for 'executionDate' must not be a past date.");
        }
      });

  }

}
