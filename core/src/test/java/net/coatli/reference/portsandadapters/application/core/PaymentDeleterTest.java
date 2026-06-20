package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.DeletePaymentPortInMapper;
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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaymentDeleterTest {

  @Mock
  private LoggingPortOut loggingPortOut;

  @Mock
  private JsonTransformationPortOut jsonTransformationPortOut;

  @Mock
  private PaymentPersistencePortOut paymentPersistencePortOut;

  private PaymentDeleter paymentDeleter;

  @BeforeEach
  void setUp() {
    Mockito.when(jsonTransformationPortOut.toJson(ArgumentMatchers.any())).thenReturn("{}");
    paymentDeleter = new PaymentDeleter(
      loggingPortOut, jsonTransformationPortOut,
      Mappers.getMapper(DeletePaymentPortInMapper.class), paymentPersistencePortOut);
  }

  @Test
  @DisplayName("Case 01: Success - Given valid reference and existing payment, When execute, Then returns output")
  void case01() {
    var uuid = "550e8400-e29b-41d4-a716-446655440000";
    var payment = new Payment().setPaymentReference(uuid);
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.of(payment));
    Mockito.when(paymentPersistencePortOut.delete(uuid)).thenReturn(payment);

    var result = paymentDeleter.execute(new DeletePaymentInput(uuid));

    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Case 02: Failure - Given null input, When execute, Then throws PaymentInputException")
  void case02() {
    Assertions.assertThrows(PaymentInputException.class, () -> paymentDeleter.execute(null));
  }

  @Test
  @DisplayName("Case 03: Failure - Given null paymentReference, When execute, Then throws PaymentInputException")
  void case03() {
    Assertions.assertThrows(PaymentInputException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput(null)));
  }

  @Test
  @DisplayName("Case 04: Failure - Given blank paymentReference, When execute, Then throws PaymentInputException")
  void case04() {
    Assertions.assertThrows(PaymentInputException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput("   ")));
  }

  @Test
  @DisplayName("Case 05: Failure - Given invalid UUID paymentReference, When execute, Then throws PaymentInputException")
  void case05() {
    Assertions.assertThrows(PaymentInputException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput("invalid-uuid")));
  }

  @Test
  @DisplayName("Case 06: Failure - Given valid UUID but payment not found, When execute, Then throws PaymentNotFoundException")
  void case06() {
    var uuid = "550e8400-e29b-41d4-a716-446655440001";
    Mockito.when(paymentPersistencePortOut.findByPaymentReference(uuid))
      .thenReturn(Optional.empty());
    Assertions.assertThrows(PaymentNotFoundException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput(uuid)));
  }

}
