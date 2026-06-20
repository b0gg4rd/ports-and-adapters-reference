package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentInput;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.UpdatePaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UpdatePaymentHandlerMapper {

  @Mapping(target = "paymentReference", source = "paymentReference")
  @Mapping(target = "paymentAmount",    source = "updatePaymentRequest.a1")
  @Mapping(target = "paymentSubject",   source = "updatePaymentRequest.a2")
  @Mapping(target = "executionDate",    source = "updatePaymentRequest.a3")
  UpdatePaymentInput mappingUpdate2UpdatePaymentInput(String paymentReference, UpdatePaymentRequest updatePaymentRequest);

}
