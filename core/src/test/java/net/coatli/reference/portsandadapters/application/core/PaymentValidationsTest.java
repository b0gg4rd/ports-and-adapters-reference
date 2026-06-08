package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentValidationsTest {

  @Test
  void requirePositiveAmount_whenNull_shouldThrow() {
    assertThrows(PaymentInputException.class,
      () -> PaymentValidations.requirePositiveAmount(null));
  }

  @Test
  void requirePositiveAmount_whenZero_shouldThrow() {
    assertThrows(PaymentInputException.class,
      () -> PaymentValidations.requirePositiveAmount(BigDecimal.ZERO));
  }

  @Test
  void requirePositiveAmount_whenNegative_shouldThrow() {
    assertThrows(PaymentInputException.class,
      () -> PaymentValidations.requirePositiveAmount(new BigDecimal("-1.00")));
  }

  @Test
  void requirePositiveAmount_whenPositive_shouldNotThrow() {
    assertDoesNotThrow(
      () -> PaymentValidations.requirePositiveAmount(new BigDecimal("100.00")));
  }

  @Test
  void requireFutureOrAbsentDate_whenNull_shouldNotThrow() {
    assertDoesNotThrow(() -> PaymentValidations.requireFutureOrAbsentDate(null));
  }

  @Test
  void requireFutureOrAbsentDate_whenPast_shouldThrow() {
    assertThrows(PaymentInputException.class,
      () -> PaymentValidations.requireFutureOrAbsentDate(LocalDateTime.now().minusDays(1)));
  }

  @Test
  void requireFutureOrAbsentDate_whenFuture_shouldNotThrow() {
    assertDoesNotThrow(
      () -> PaymentValidations.requireFutureOrAbsentDate(LocalDateTime.now().plusDays(1)));
  }

}
