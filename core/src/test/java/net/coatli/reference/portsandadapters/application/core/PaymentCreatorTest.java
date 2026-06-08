package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.CreatePaymentPortInMapper;
import org.mapstruct.factory.Mappers;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.domain.enums.PaymentStatus;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentCreatorTest {

  @Mock
  private PaymentPersistencePortOut paymentPersistencePortOut;

  private PaymentCreator paymentCreator;

  @BeforeEach
  void setUp() {
    paymentCreator = new PaymentCreator(Mappers.getMapper(CreatePaymentPortInMapper.class), paymentPersistencePortOut);
  }

  @Test
  void execute_whenNullInput_shouldThrow() {
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(null));
  }

  @Test
  void execute_whenNullPayerReference_shouldThrow() {
    var input = new CreatePaymentInput(null, "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  void execute_whenBlankPayerReference_shouldThrow() {
    var input = new CreatePaymentInput("   ", "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  void execute_whenNullPayeeReference_shouldThrow() {
    var input = new CreatePaymentInput("payer-1", null, new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  void execute_whenBlankPayeeReference_shouldThrow() {
    var input = new CreatePaymentInput("payer-1", "   ", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().plusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  void execute_whenNullPaymentAmount_shouldThrow() {
    var input = new CreatePaymentInput("payer-1", "payee-1", null, "subject",
      LocalDateTime.now().plusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  void execute_whenZeroPaymentAmount_shouldThrow() {
    var input = new CreatePaymentInput("payer-1", "payee-1", BigDecimal.ZERO, "subject",
      LocalDateTime.now().plusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  void execute_whenNegativePaymentAmount_shouldThrow() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("-50.00"), "subject",
      LocalDateTime.now().plusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  void execute_whenPastExecutionDate_shouldThrow() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject",
      LocalDateTime.now().minusDays(1));
    assertThrows(PaymentInputException.class, () -> paymentCreator.execute(input));
  }

  @Test
  void execute_whenNullExecutionDate_shouldEnrichWithNowAndReturnOutput() {
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject", null);
    var captor = ArgumentCaptor.forClass(Payment.class);

    when(paymentPersistencePortOut.create(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var result = paymentCreator.execute(input);

    assertNotNull(result.paymentReference());
    var enriched = captor.getValue();
    assertEquals(PaymentStatus.PENDING, enriched.getStatus());
    assertNotNull(enriched.getExecutionDate());
    assertNotNull(enriched.getCreatedAt());
  }

  @Test
  void execute_whenFutureExecutionDate_shouldPreserveExecutionDateAndReturnOutput() {
    var futureDate = LocalDateTime.now().plusDays(1);
    var input = new CreatePaymentInput("payer-1", "payee-1", new BigDecimal("100.00"), "subject",
      futureDate);
    var captor = ArgumentCaptor.forClass(Payment.class);

    when(paymentPersistencePortOut.create(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var result = paymentCreator.execute(input);

    assertNotNull(result.paymentReference());
    assertEquals(futureDate, captor.getValue().getExecutionDate());
  }

}
