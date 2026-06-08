package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.RetrieveAllPaymentsPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.domain.model.Page;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
class PaymentAllRetrieverTest {

  @Mock
  private PaymentPersistencePortOut paymentPersistencePortOut;

  private PaymentAllRetriever paymentAllRetriever;

  @BeforeEach
  void setUp() {
    paymentAllRetriever = new PaymentAllRetriever(
      Mappers.getMapper(RetrieveAllPaymentsPortInMapper.class), paymentPersistencePortOut);
  }

  @Test
  @DisplayName("Caso 01: Éxito - Given pagos existentes, When execute, Then retorna página con pagos")
  void case01() {
    var payment = new Payment().setPaymentReference("ref-1");
    var pagedResult = new Page<Payment>()
      .setContent(List.of(payment))
      .setTotalElements(1L)
      .setTotalPages(1);
    var captor = ArgumentCaptor.forClass(Page.class);
    Mockito.when(paymentPersistencePortOut.retrieveAll(captor.capture())).thenReturn(pagedResult);

    var result = paymentAllRetriever.execute(new RetrieveAllPaymentsInput(0, 10));

    Assertions.assertEquals(1, result.payments().getContent().size());
    Assertions.assertEquals("ref-1", result.payments().getContent().get(0).getPaymentReference());
    Assertions.assertEquals(1L, result.payments().getTotalElements());
    Assertions.assertEquals(0, captor.getValue().getPagination().getPage());
    Assertions.assertEquals(10, captor.getValue().getPagination().getSize());
  }

  @Test
  @DisplayName("Caso 02: Éxito - Given sin pagos registrados, When execute, Then retorna página vacía")
  void case02() {
    var pagedResult = new Page<Payment>()
      .setContent(List.of())
      .setTotalElements(0L)
      .setTotalPages(0);
    Mockito.when(paymentPersistencePortOut.retrieveAll(ArgumentMatchers.any(Page.class))).thenReturn(pagedResult);

    var result = paymentAllRetriever.execute(new RetrieveAllPaymentsInput(1, 20));

    Assertions.assertTrue(result.payments().getContent().isEmpty());
    Assertions.assertEquals(0L, result.payments().getTotalElements());
  }

  @Test
  @DisplayName("Caso 03: Fallo - Given input nulo, When execute, Then lanza NoSuchElementException")
  void case03() {
    Assertions.assertThrows(NoSuchElementException.class, () -> paymentAllRetriever.execute(null));
  }

}
