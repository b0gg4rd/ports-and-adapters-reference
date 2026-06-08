package net.coatli.reference.portsandadapters.application.port.in.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentOutput;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UpdatePaymentPortInMapper {

  @Mapping(target = "paymentReference", source = "paymentReference")
  UpdatePaymentOutput mappingPayment2UpdatePaymentOutput(Payment payment);

}
