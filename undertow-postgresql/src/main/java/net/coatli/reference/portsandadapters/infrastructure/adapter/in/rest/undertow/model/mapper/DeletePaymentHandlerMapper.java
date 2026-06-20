package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeletePaymentHandlerMapper {

  @Mapping(target = "paymentReference", source = "paymentReference")
  DeletePaymentInput mappingPaymentReference2DeletePaymentInput(String paymentReference);

}
