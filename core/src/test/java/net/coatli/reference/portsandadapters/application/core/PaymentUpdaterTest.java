package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.UpdatePaymentPortInMapper;
import org.mapstruct.factory.Mappers;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentUpdaterTest {

  @Mock
  private PaymentPersistencePortOut paymentPersistencePortOut;

  private PaymentUpdater paymentUpdater;

  @BeforeEach
  void setUp() {
    paymentUpdater = new PaymentUpdater(Mappers.getMapper(UpdatePaymentPortInMapper.class), paymentPersistencePortOut);
  }

  @Test
  void execute_whenNullInput_shouldThrow() {
    assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(null));
  }

  @Test
  void execute_whenNullPayeeReference_shouldThrow() {
    var input = new UpdatePaymentInput(null, new BigDecimal("100.00"), "subject", null);
    assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  void execute_whenBlankPayeeReference_shouldThrow() {
    var input = new UpdatePaymentInput("   ", new BigDecimal("100.00"), "subject", null);
    assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  void execute_whenPastExecutionDate_shouldThrow() {
    var input = new UpdatePaymentInput("payee-1", null, null, LocalDateTime.now().minusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  void execute_whenPaymentNotFound_shouldThrow() {
    var input = new UpdatePaymentInput("payee-1", null, null, null);
    when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.empty());
    assertThrows(PaymentNotFoundException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  void execute_whenPersistenceUpdateReturnsEmpty_shouldThrow() {
    var input = new UpdatePaymentInput("payee-1", null, null, null);
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.update(any())).thenReturn(Optional.empty());
    assertThrows(IllegalStateException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  void execute_whenAllFieldsNull_shouldUpdateAndReturnOutput() {
    var input = new UpdatePaymentInput("payee-1", null, null, null);
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    var result = paymentUpdater.execute(input);

    assertEquals("ref-1", result.paymentReference());
  }

  @Test
  void execute_whenPositiveAmount_shouldUpdateAmount() {
    var newAmount = new BigDecimal("250.00");
    var input = new UpdatePaymentInput("payee-1", newAmount, null, null);
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    paymentUpdater.execute(input);

    assertEquals(newAmount, payment.getPaymentAmount());
  }

  @Test
  void execute_whenZeroAmount_shouldThrow() {
    var input = new UpdatePaymentInput("payee-1", BigDecimal.ZERO, null, null);
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  void execute_whenNonBlankSubject_shouldUpdateSubject() {
    var input = new UpdatePaymentInput("payee-1", null, "new subject", null);
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    paymentUpdater.execute(input);

    assertEquals("new subject", payment.getPaymentSubject());
  }

  @Test
  void execute_whenBlankSubject_shouldNotUpdateSubject() {
    var input = new UpdatePaymentInput("payee-1", null, "   ", null);
    var payment = new Payment().setPaymentReference("ref-1").setPaymentSubject("original");
    when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    paymentUpdater.execute(input);

    assertEquals("original", payment.getPaymentSubject());
  }

  @Test
  void execute_whenFutureExecutionDate_shouldUpdateExecutionDate() {
    var futureDate = LocalDateTime.now().plusDays(1);
    var input = new UpdatePaymentInput("payee-1", null, null, futureDate);
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    paymentUpdater.execute(input);

    assertEquals(futureDate, payment.getExecutionDate());
  }

}
