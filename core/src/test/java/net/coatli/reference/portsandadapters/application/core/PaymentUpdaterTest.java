package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.UpdatePaymentPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaymentUpdaterTest {

  @Mock
  private LoggingPortOut loggingPortOut;

  @Mock
  private JsonTransformationPortOut jsonTransformationPortOut;

  @Mock
  private PaymentPersistencePortOut paymentPersistencePortOut;

  private PaymentUpdater paymentUpdater;

  @BeforeEach
  void setUp() {
    Mockito.when(jsonTransformationPortOut.toJson(ArgumentMatchers.any())).thenReturn("{}");
    paymentUpdater = new PaymentUpdater(
      loggingPortOut, jsonTransformationPortOut,
      Mappers.getMapper(UpdatePaymentPortInMapper.class), paymentPersistencePortOut);
  }

  @Test
  @DisplayName("Case 01: Success - Given all optional fields null, When execute, Then updates and returns output")
  void case01() {
    var uuid = "550e8400-e29b-41d4-a716-446655440000";
    var input = new UpdatePaymentInput(uuid, null, null, null);
    var payment = new Payment().setPaymentReference(uuid);
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(payment);

    var result = paymentUpdater.execute(input);

    Assertions.assertEquals(uuid, result.paymentReference());
  }

  @Test
  @DisplayName("Case 02: Success - Given positive paymentAmount, When execute, Then updates the amount")
  void case02() {
    var uuid = "550e8400-e29b-41d4-a716-446655440000";
    var newAmount = new BigDecimal("250.00");
    var input = new UpdatePaymentInput(uuid, newAmount, null, null);
    var payment = new Payment().setPaymentReference(uuid);
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(payment);

    paymentUpdater.execute(input);

    Assertions.assertEquals(newAmount, payment.getPaymentAmount());
  }

  @Test
  @DisplayName("Case 03: Success - Given non-empty paymentSubject, When execute, Then updates the subject")
  void case03() {
    var uuid = "550e8400-e29b-41d4-a716-446655440000";
    var input = new UpdatePaymentInput(uuid, null, "new subject", null);
    var payment = new Payment().setPaymentReference(uuid);
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(payment);

    paymentUpdater.execute(input);

    Assertions.assertEquals("new subject", payment.getPaymentSubject());
  }

  @Test
  @DisplayName("Case 04: Success - Given blank paymentSubject, When execute, Then preserves original subject")
  void case04() {
    var uuid = "550e8400-e29b-41d4-a716-446655440000";
    var input = new UpdatePaymentInput(uuid, null, "   ", null);
    var payment = new Payment().setPaymentReference(uuid).setPaymentSubject("original");
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(payment);

    paymentUpdater.execute(input);

    Assertions.assertEquals("original", payment.getPaymentSubject());
  }

  @Test
  @DisplayName("Case 05: Success - Given future executionDate, When execute, Then updates the execution date")
  void case05() {
    var uuid = "550e8400-e29b-41d4-a716-446655440000";
    var futureDate = LocalDateTime.now().plusDays(1);
    var input = new UpdatePaymentInput(uuid, null, null, futureDate);
    var payment = new Payment().setPaymentReference(uuid);
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(payment);

    paymentUpdater.execute(input);

    Assertions.assertEquals(futureDate, payment.getExecutionDate());
  }

  @Test
  @DisplayName("Case 06: Failure - Given null input, When execute, Then throws PaymentInputException")
  void case06() {
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(null));
  }

  @Test
  @DisplayName("Case 07: Failure - Given null paymentReference, When execute, Then throws PaymentInputException")
  void case07() {
    var input = new UpdatePaymentInput(null, new BigDecimal("100.00"), "subject", null);
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Case 08: Failure - Given blank paymentReference, When execute, Then throws PaymentInputException")
  void case08() {
    var input = new UpdatePaymentInput("   ", new BigDecimal("100.00"), "subject", null);
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Case 09: Failure - Given past executionDate, When execute, Then throws PaymentInputException")
  void case09() {
    var input = new UpdatePaymentInput("payee-1", null, null, LocalDateTime.now().minusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Case 10: Failure - Given payment not found, When execute, Then throws PaymentNotFoundException")
  void case10() {
    var uuid = "550e8400-e29b-41d4-a716-446655440001";
    var input = new UpdatePaymentInput(uuid, null, null, null);
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.empty());
    Assertions.assertThrows(PaymentNotFoundException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Case 11: Failure - Given zero paymentAmount, When execute, Then throws PaymentInputException")
  void case11() {
    var uuid = "550e8400-e29b-41d4-a716-446655440000";
    var input = new UpdatePaymentInput(uuid, BigDecimal.ZERO, null, null);
    var payment = new Payment().setPaymentReference(uuid);
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.of(payment));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Case 12: Failure - Given invalid UUID paymentReference, When execute, Then throws PaymentInputException")
  void case12() {
    var input = new UpdatePaymentInput("invalid-uuid", null, null, null);
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

}
