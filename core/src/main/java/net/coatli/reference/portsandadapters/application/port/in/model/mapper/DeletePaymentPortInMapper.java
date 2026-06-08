package net.coatli.reference.portsandadapters.application.port.in.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentOutput;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.mapstruct.Mapper;

@Mapper
public interface DeletePaymentPortInMapper {

  DeletePaymentOutput mappingPayment2DeletePaymentOutput(Payment payment);

}
