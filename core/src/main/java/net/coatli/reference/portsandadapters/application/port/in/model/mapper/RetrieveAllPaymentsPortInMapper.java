package net.coatli.reference.portsandadapters.application.port.in.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsOutput;
import net.coatli.reference.portsandadapters.domain.model.Page;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RetrieveAllPaymentsPortInMapper {

  @Mapping(target = "payments", source = "payments")
  RetrieveAllPaymentsOutput mappingPayments2RetrieveAllPaymentsOutput(Page<Payment> payments);

}
