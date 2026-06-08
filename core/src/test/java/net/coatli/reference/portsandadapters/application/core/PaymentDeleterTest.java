package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.DeletePaymentPortInMapper;
import org.mapstruct.factory.Mappers;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("Caso 01: Fallo - Given input nulo, When execute, Then lanza PaymentInputException")
  void case01() {
    assertThrows(PaymentInputException.class, () -> paymentDeleter.execute(null));
  }

  @Test
  @DisplayName("Caso 02: Fallo - Given 'paymentReference' nulo, When execute, Then lanza PaymentInputException")
  void case02() {
    assertThrows(PaymentInputException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput(null)));
  }

  @Test
  @DisplayName("Caso 03: Fallo - Given 'paymentReference' en blanco, When execute, Then lanza PaymentInputException")
  void case03() {
    assertThrows(PaymentInputException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput("   ")));
  }

  @Test
  @DisplayName("Caso 04: Fallo - Given pago no encontrado, When execute, Then lanza PaymentNotFoundException")
  void case04() {
    when(paymentPersistencePortOut.findByPaymentReference("ref-1"))
      .thenReturn(Optional.empty());
    assertThrows(PaymentNotFoundException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput("ref-1")));
  }

  @Test
  @DisplayName("Caso 05: Fallo - Given persistencia retorna vacío en delete, When execute, Then lanza IllegalStateException")
  void case05() {
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("ref-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.delete("ref-1")).thenReturn(Optional.empty());
    assertThrows(IllegalStateException.class,
      () -> paymentDeleter.execute(new DeletePaymentInput("ref-1")));
  }

  @Test
  @DisplayName("Caso 06: Éxito - Given referencia válida y pago existente, When execute, Then retorna output")
  void case06() {
    var payment = new Payment().setPaymentReference("ref-1");
    when(paymentPersistencePortOut.findByPaymentReference("ref-1"))
      .thenReturn(Optional.of(payment));
    when(paymentPersistencePortOut.delete("ref-1")).thenReturn(Optional.of(payment));

    var result = paymentDeleter.execute(new DeletePaymentInput("ref-1"));

    assertNotNull(result);
  }

}
