package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.CreatePaymentPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.domain.enums.PaymentStatus;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class PaymentCreatorTest {

  @Mock
  private LoggingPortOut loggingPortOut;

  @Mock
  private JsonTransformationPortOut jsonTransformationPortOut;

  @Mock
  private PaymentPersistencePortOut paymentPersistencePortOut;

  private PaymentCreator paymentCreator;

  @BeforeEach
  void setUp() {
    Mockito.when(jsonTransformationPortOut.toJson(ArgumentMatchers.any())).thenReturn("{}");
    paymentCreator = new PaymentCreator(
      loggingPortOut, jsonTransformationPortOut,
      Mappers.getMapper(CreatePaymentPortInMapper.class), paymentPersistencePortOut);
  }

  @Test
  @DisplayName("Case 01: Success - Given null executionDate, When execute, Then enriches with current date and returns output")
  void case01() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject", null);
    var captor = ArgumentCaptor.forClass(Payment.class);

    Mockito.when(paymentPersistencePortOut.create(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var result = paymentCreator.execute(input);

    Assertions.assertNotNull(result.paymentReference());
    var enriched = captor.getValue();
    Assertions.assertEquals(PaymentStatus.PENDING, enriched.getStatus());
    Assertions.assertNotNull(enriched.getExecutionDate());
    Assertions.assertNotNull(enriched.getCreatedAt());
  }

  @Test
  @DisplayName("Case 02: Success - Given future executionDate, When execute, Then preserves date and returns output")
  void case02() {
    var futureDate = LocalDateTime.now().plusDays(1);
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject",
      futureDate);
    var captor = ArgumentCaptor.forClass(Payment.class);

    Mockito.when(paymentPersistencePortOut.create(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var result = paymentCreator.execute(input);

    Assertions.assertNotNull(result.paymentReference());
    Assertions.assertEquals(futureDate, captor.getValue().getExecutionDate());
  }

  @Test
  @DisplayName("Case 03: Failure - Given null input, When execute, Then throws PaymentInputException")
  void case03() {
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(null));
  }

  @Test
  @DisplayName("Case 04: Failure - Given null payerReference, When execute, Then throws PaymentInputException")
  void case04() {
    var input = new CreatePaymentInput(null, "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Case 05: Failure - Given blank payerReference, When execute, Then throws PaymentInputException")
  void case05() {
    var input = new CreatePaymentInput("   ", "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Case 06: Failure - Given null payeeReference, When execute, Then throws PaymentInputException")
  void case06() {
    var input = new CreatePaymentInput("payer-1", null, new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Case 07: Failure - Given blank payeeReference, When execute, Then throws PaymentInputException")
  void case07() {
    var input = new CreatePaymentInput("payer-1", "   ", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Case 08: Failure - Given null paymentAmount, When execute, Then throws PaymentInputException")
  void case08() {
    var input = new CreatePaymentInput("payer-1", "payee-1", null, "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Case 09: Failure - Given zero paymentAmount, When execute, Then throws PaymentInputException")
  void case09() {
    var input = new CreatePaymentInput("payer-1", "payee-1", BigDecimal.ZERO, "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Case 10: Failure - Given negative paymentAmount, When execute, Then throws PaymentInputException")
  void case10() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("-50.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Case 11: Failure - Given past executionDate, When execute, Then throws PaymentInputException")
  void case11() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().minusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Case 12: Failure - Given persistence returns null, When execute, Then throws IllegalStateException")
  void case12() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Mockito.when(paymentPersistencePortOut.create(ArgumentMatchers.any(Payment.class))).thenReturn(null);

    Assertions.assertThrows(IllegalStateException.class, () -> paymentCreator.execute(input));
  }

}
