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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface RetrieveAllPaymentsHandlerMapper {

  default RetrieveAllPaymentsInput mappingQueryParams2RetrieveAllPaymentsInput(String page, String size) {
    return new RetrieveAllPaymentsInput(page, size);
  }

  @Mapping(target = "a0", source = "paymentReference")
  @Mapping(target = "a1", source = "payerReference")
  @Mapping(target = "a2", source = "payeeReference")
  @Mapping(target = "a3", source = "paymentAmount", qualifiedByName = "bigDecimalToString")
  @Mapping(target = "a4", source = "paymentSubject")
  @Mapping(target = "a5", source = "executionDate", qualifiedByName = "localDateTimeToString")
  @Mapping(target = "a6", source = "status")
  @Mapping(target = "a7", source = "createdAt", qualifiedByName = "localDateTimeToString")
  RetrieveAllPaymentsItemResponse mappingPayment2PaymentData(Payment payment);

  @Mapping(target = "a0", source = "payments.pagination.page")
  @Mapping(target = "a1", source = "payments.pagination.size")
  @Mapping(target = "a2", source = "payments.totalElements")
  @Mapping(target = "a3", source = "payments.totalPages")
  @Mapping(target = "a4", source = "payments.content")
  RetrieveAllPaymentsResponse mappingRetrieveAllPaymentsOutput2RetrieveAllPaymentsResponse(RetrieveAllPaymentsOutput retrieveAllPaymentsOutput);

  @Named("bigDecimalToString")
  default String bigDecimalToString(BigDecimal value) {
    return value != null ? value.toPlainString() : null;
  }

  @Named("localDateTimeToString")
  default String localDateTimeToString(LocalDateTime value) {
    return value != null ? value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
  }

}
