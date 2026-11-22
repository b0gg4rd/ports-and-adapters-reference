package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentInput;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.CreatePaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CreatePaymentHandlerMapper {

  @Mapping(target = "payerReference",  source = "payerReference")
  @Mapping(target = "payeeReference",  source = "payeeReference")
  @Mapping(target = "paymentAmount",   source = "paymentAmount")
  @Mapping(target = "paymentSubject",  source = "paymentSubject")
  @Mapping(target = "executionDate",   source = "executionDate")
  CreatePaymentInput mappingCreatePaymentRequest2CreatePaymentInput(CreatePaymentRequest createPaymentRequest);

}
