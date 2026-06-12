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
  @DisplayName("Caso 01: Fallo - Given input nulo, When execute, Then lanza PaymentInputException")
  void case01() {
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(null));
  }

  @Test
  @DisplayName("Caso 02: Fallo - Given 'paymentReference' nulo, When execute, Then lanza PaymentInputException")
  void case02() {
    var input = new UpdatePaymentInput(null, new BigDecimal("100.00"), "subject", null);
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Caso 03: Fallo - Given 'paymentReference' en blanco, When execute, Then lanza PaymentInputException")
  void case03() {
    var input = new UpdatePaymentInput("   ", new BigDecimal("100.00"), "subject", null);
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Caso 04: Fallo - Given 'executionDate' en el pasado, When execute, Then lanza PaymentInputException")
  void case04() {
    var input = new UpdatePaymentInput("payee-1", null, null, LocalDateTime.now().minusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Caso 05: Fallo - Given pago no encontrado, When execute, Then lanza PaymentNotFoundException")
  void case05() {
    var input = new UpdatePaymentInput("payee-1", null, null, null);
    Mockito.when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.empty());
    Assertions.assertThrows(PaymentNotFoundException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Caso 06: Fallo - Given persistencia retorna vacío en update, When execute, Then lanza IllegalStateException")
  void case06() {
    var input = new UpdatePaymentInput("payee-1", null, null, null);
    var payment = new Payment().setPaymentReference("ref-1");
    Mockito.when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(ArgumentMatchers.any())).thenReturn(Optional.empty());
    Assertions.assertThrows(IllegalStateException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Caso 07: Éxito - Given todos los campos opcionales nulos, When execute, Then actualiza y retorna output")
  void case07() {
    var input = new UpdatePaymentInput("payee-1", null, null, null);
    var payment = new Payment().setPaymentReference("ref-1");
    Mockito.when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    var result = paymentUpdater.execute(input);

    Assertions.assertEquals("ref-1", result.paymentReference());
  }

  @Test
  @DisplayName("Caso 08: Éxito - Given 'paymentAmount' positivo, When execute, Then actualiza el monto")
  void case08() {
    var newAmount = new BigDecimal("250.00");
    var input = new UpdatePaymentInput("payee-1", newAmount, null, null);
    var payment = new Payment().setPaymentReference("ref-1");
    Mockito.when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    paymentUpdater.execute(input);

    Assertions.assertEquals(newAmount, payment.getPaymentAmount());
  }

  @Test
  @DisplayName("Caso 09: Fallo - Given 'paymentAmount' en cero, When execute, Then lanza PaymentInputException")
  void case09() {
    var input = new UpdatePaymentInput("payee-1", BigDecimal.ZERO, null, null);
    var payment = new Payment().setPaymentReference("ref-1");
    Mockito.when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentUpdater.execute(input));
  }

  @Test
  @DisplayName("Caso 10: Éxito - Given 'paymentSubject' no vacío, When execute, Then actualiza el subject")
  void case10() {
    var input = new UpdatePaymentInput("payee-1", null, "new subject", null);
    var payment = new Payment().setPaymentReference("ref-1");
    Mockito.when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    paymentUpdater.execute(input);

    Assertions.assertEquals("new subject", payment.getPaymentSubject());
  }

  @Test
  @DisplayName("Caso 11: Éxito - Given 'paymentSubject' en blanco, When execute, Then no actualiza el subject")
  void case11() {
    var input = new UpdatePaymentInput("payee-1", null, "   ", null);
    var payment = new Payment().setPaymentReference("ref-1").setPaymentSubject("original");
    Mockito.when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    paymentUpdater.execute(input);

    Assertions.assertEquals("original", payment.getPaymentSubject());
  }

  @Test
  @DisplayName("Caso 12: Éxito - Given 'executionDate' futuro, When execute, Then actualiza la fecha de ejecución")
  void case12() {
    var futureDate = LocalDateTime.now().plusDays(1);
    var input = new UpdatePaymentInput("payee-1", null, null, futureDate);
    var payment = new Payment().setPaymentReference("ref-1");
    Mockito.when(paymentPersistencePortOut.findByPaymentReference("payee-1"))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.update(payment)).thenReturn(Optional.of(payment));

    paymentUpdater.execute(input);

    Assertions.assertEquals(futureDate, payment.getExecutionDate());
  }

}
