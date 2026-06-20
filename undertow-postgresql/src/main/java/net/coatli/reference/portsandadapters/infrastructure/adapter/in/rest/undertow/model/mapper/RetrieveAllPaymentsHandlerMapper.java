package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsOutput;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.PaymentData;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.RetrieveAllPaymentsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;

@Mapper
public interface RetrieveAllPaymentsHandlerMapper {

  default RetrieveAllPaymentsInput mappingQueryParams2RetrieveAllPaymentsInput(String page, String size) {

    return new RetrieveAllPaymentsInput(
      page != null ? Integer.parseInt(page) : 0,
      size != null ? Integer.parseInt(size) : 10);

  }

  @Mapping(target = "a0", source = "paymentReference")
  @Mapping(target = "a1", source = "payerReference")
  @Mapping(target = "a2", source = "payeeReference")
  @Mapping(target = "a3", expression = "java(payment.getPaymentAmount() != null ? payment.getPaymentAmount().toPlainString() : null)")
  @Mapping(target = "a4", source = "paymentSubject")
  @Mapping(target = "a5", expression = "java(payment.getExecutionDate() != null ? payment.getExecutionDate().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null)")
  @Mapping(target = "a6", expression = "java(payment.getStatus() != null ? payment.getStatus().name() : null)")
  @Mapping(target = "a7", expression = "java(payment.getCreatedAt() != null ? payment.getCreatedAt().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null)")
  PaymentData mappingPayment2PaymentData(Payment payment);

  default RetrieveAllPaymentsResponse mappingRetrieveAllPaymentsOutput2RetrieveAllPaymentsResponse(RetrieveAllPaymentsOutput retrieveAllPaymentsOutput) {

    return new RetrieveAllPaymentsResponse(
      retrieveAllPaymentsOutput.payments().getPagination().getPage(),
      retrieveAllPaymentsOutput.payments().getPagination().getSize(),
      retrieveAllPaymentsOutput.payments().getTotalElements(),
      retrieveAllPaymentsOutput.payments().getTotalPages(),
      retrieveAllPaymentsOutput.payments().getContent().stream()
        .map(this::mappingPayment2PaymentData)
        .toList());

  }

}
