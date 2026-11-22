package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentInput;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.UpdatePaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UpdatePaymentHandlerMapper {

  @Mapping(target = "paymentReference", source = "paymentReference")
  @Mapping(target = "paymentAmount",    source = "updatePaymentRequest.paymentAmount")
  @Mapping(target = "paymentSubject",   source = "updatePaymentRequest.paymentSubject")
  @Mapping(target = "executionDate",    source = "updatePaymentRequest.executionDate")
  UpdatePaymentInput mappingUpdate2UpdatePaymentInput(String paymentReference, UpdatePaymentRequest updatePaymentRequest);

}
