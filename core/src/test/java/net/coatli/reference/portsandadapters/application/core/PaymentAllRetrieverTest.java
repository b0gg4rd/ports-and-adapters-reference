package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.RetrieveAllPaymentsPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
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
@ExtendWith(MockitoExtension.class)
class PaymentAllRetrieverTest {

  @Mock
  private LoggingPortOut loggingPortOut;

  @Mock
  private JsonTransformationPortOut jsonTransformationPortOut;

  @Mock
  private PaymentPersistencePortOut paymentPersistencePortOut;

  private PaymentAllRetriever paymentAllRetriever;

  @BeforeEach
  void setUp() {
    paymentAllRetriever = new PaymentAllRetriever(
      loggingPortOut,
      jsonTransformationPortOut,
      Mappers.getMapper(RetrieveAllPaymentsPortInMapper.class),
      paymentPersistencePortOut);
  }

  @Test
  @DisplayName("Case 01: Success - Given existing payments, When execute, Then returns page with payments")
  void case01() {
    var payment = new Payment().setPaymentReference("ref-1");
    var pagedResult = new Page<Payment>()
      .setContent(List.of(payment))
      .setTotalElements(1L)
      .setTotalPages(1);
    var captor = ArgumentCaptor.forClass(Page.class);
    Mockito.when(paymentPersistencePortOut.retrieveAll(captor.capture())).thenReturn(pagedResult);

    var result = paymentAllRetriever.execute(new RetrieveAllPaymentsInput("0", "10"));

    Assertions.assertEquals(1, result.payments().getContent().size());
    Assertions.assertEquals("ref-1", result.payments().getContent().get(0).getPaymentReference());
    Assertions.assertEquals(1L, result.payments().getTotalElements());
    Assertions.assertEquals(0, captor.getValue().getPagination().getPage());
    Assertions.assertEquals(10, captor.getValue().getPagination().getSize());
  }

  @Test
  @DisplayName("Case 02: Success - Given no registered payments, When execute, Then returns empty page")
  void case02() {
    var pagedResult = new Page<Payment>()
      .setContent(List.of())
      .setTotalElements(0L)
      .setTotalPages(0);
    Mockito.when(paymentPersistencePortOut.retrieveAll(ArgumentMatchers.any(Page.class))).thenReturn(pagedResult);

    var result = paymentAllRetriever.execute(new RetrieveAllPaymentsInput("1", "20"));

    Assertions.assertTrue(result.payments().getContent().isEmpty());
    Assertions.assertEquals(0L, result.payments().getTotalElements());
  }

  @Test
  @DisplayName("Case 03: Failure - Given null input, When execute, Then throws PaymentInputException")
  void case03() {
    Assertions.assertThrows(PaymentInputException.class, () -> paymentAllRetriever.execute(null));
  }

}
