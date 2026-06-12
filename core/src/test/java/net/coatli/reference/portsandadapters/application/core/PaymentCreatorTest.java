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
  @DisplayName("Caso 01: Fallo - Given input nulo, When execute, Then lanza PaymentInputException")
  void case01() {
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(null));
  }

  @Test
  @DisplayName("Caso 02: Fallo - Given 'payerReference' nulo, When execute, Then lanza PaymentInputException")
  void case02() {
    var input = new CreatePaymentInput(null, "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Caso 03: Fallo - Given 'payerReference' en blanco, When execute, Then lanza PaymentInputException")
  void case03() {
    var input = new CreatePaymentInput("   ", "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Caso 04: Fallo - Given 'payeeReference' nulo, When execute, Then lanza PaymentInputException")
  void case04() {
    var input = new CreatePaymentInput("payer-1", null, new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Caso 05: Fallo - Given 'payeeReference' en blanco, When execute, Then lanza PaymentInputException")
  void case05() {
    var input = new CreatePaymentInput("payer-1", "   ", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Caso 06: Fallo - Given 'paymentAmount' nulo, When execute, Then lanza PaymentInputException")
  void case06() {
    var input = new CreatePaymentInput("payer-1", "payee-1", null, "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Caso 07: Fallo - Given 'paymentAmount' en cero, When execute, Then lanza PaymentInputException")
  void case07() {
    var input = new CreatePaymentInput("payer-1", "payee-1", BigDecimal.ZERO, "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Caso 08: Fallo - Given 'paymentAmount' negativo, When execute, Then lanza PaymentInputException")
  void case08() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("-50.00"), "subject",
      LocalDateTime.now().plusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Caso 09: Fallo - Given 'executionDate' en el pasado, When execute, Then lanza PaymentInputException")
  void case09() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().minusDays(1));
    Assertions.assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  @DisplayName("Caso 10: Éxito - Given 'executionDate' nulo, When execute, Then enriquece con fecha actual y retorna output")
  void case10() {
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
  @DisplayName("Caso 11: Éxito - Given 'executionDate' futuro, When execute, Then preserva la fecha y retorna output")
  void case11() {
    var futureDate = LocalDateTime.now().plusDays(1);
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject",
      futureDate);
    var captor = ArgumentCaptor.forClass(Payment.class);

    Mockito.when(paymentPersistencePortOut.create(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var result = paymentCreator.execute(input);

    Assertions.assertNotNull(result.paymentReference());
    Assertions.assertEquals(futureDate, captor.getValue().getExecutionDate());
  }

}
