package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.DeletePaymentPortInMapper;
import org.mapstruct.factory.Mappers;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentDeleterTest {

  @Mock
  private PaymentPersistencePortOut paymentPersistencePortOut;

  private PaymentDeleter paymentDeleter;

  @BeforeEach
  void setUp() {
    paymentDeleter = new PaymentDeleter(Mappers.getMapper(DeletePaymentPortInMapper.class), paymentPersistencePortOut);
  }

  @Test
  void execute_whenNullInput_shouldThrow() {
    assertThrows(PaymentInputException.class, () -> paymentDeleter.execute(null));
  }

  @Test
  void execute_whenNullPaymentReference_shouldThrow() {
    assertThrows(PaymentInputException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput(null)));
  }

  @Test
  void execute_whenBlankPaymentReference_shouldThrow() {
    assertThrows(PaymentInputException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput("   ")));
  }

  @Test
  void execute_whenPaymentNotFound_shouldThrow() {
    when(paymentPersistencePortOut.findByPaymentReference("ref-1"))
      .thenReturn(Optional.empty());
    assertThrows(PaymentNotFoundException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput("ref-1")));
  }

  @Test
  void execute_whenPersistenceDeleteReturnsEmpty_shouldThrow() {
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("ref-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.delete("ref-1")).thenReturn(Optional.empty());
    assertThrows(IllegalStateException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput("ref-1")));
  }

  @Test
  void execute_whenValidReference_shouldReturnOutput() {
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("ref-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.delete("ref-1")).thenReturn(Optional.of(payment));

    var result = paymentDeleter.execute(new DeletePaymentInput("ref-1"));

    assertNotNull(result);
  }

}
