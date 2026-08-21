package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentInput;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.CreatePaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CreatePaymentHandlerMapper {

  @Mapping(target = "payerReference",  source = "a1")
  @Mapping(target = "payeeReference",  source = "a2")
  @Mapping(target = "paymentAmount",   source = "a3")
  @Mapping(target = "paymentSubject",  source = "a4")
  @Mapping(target = "executionDate",   source = "a5")
  CreatePaymentInput mappingCreatePaymentRequest2CreatePaymentInput(CreatePaymentRequest createPaymentRequest);

}
