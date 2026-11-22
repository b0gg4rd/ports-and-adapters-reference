package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsOutput;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.RetrieveAllPaymentsItemResponse;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.RetrieveAllPaymentsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper
public interface RetrieveAllPaymentsHandlerMapper {

  default RetrieveAllPaymentsInput mappingQueryParams2RetrieveAllPaymentsInput(String page, String size) {
    return new RetrieveAllPaymentsInput(page, size);
  }

  @Mapping(target = "paymentReference", source = "paymentReference")
  @Mapping(target = "payerReference", source = "payerReference")
  @Mapping(target = "payeeReference", source = "payeeReference")
  @Mapping(target = "paymentAmount", source = "paymentAmount", qualifiedByName = "bigDecimalToString")
  @Mapping(target = "paymentSubject", source = "paymentSubject")
  @Mapping(target = "executionDate", source = "executionDate")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "createdAt", source = "createdAt")
  RetrieveAllPaymentsItemResponse mappingPayment2PaymentData(Payment payment);

  @Mapping(target = "page", source = "payments.pagination.page")
  @Mapping(target = "size", source = "payments.pagination.size")
  @Mapping(target = "totalElements", source = "payments.totalElements")
  @Mapping(target = "totalPages", source = "payments.totalPages")
  @Mapping(target = "content", source = "payments.content")
  RetrieveAllPaymentsResponse mappingRetrieveAllPaymentsOutput2RetrieveAllPaymentsResponse(RetrieveAllPaymentsOutput retrieveAllPaymentsOutput);

  @Named("bigDecimalToString")
  default String bigDecimalToString(BigDecimal value) {
    return value != null ? value.toPlainString() : null;
  }

}
