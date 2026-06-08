package net.coatli.reference.portsandadapters.application.port.in.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentOutput;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CreatePaymentPortInMapper {

  @Mapping(target = "paymentReference", ignore = true)
  @Mapping(target = "payerReference",   source = "payerReference")
  @Mapping(target = "payeeReference",   source = "payeeReference")
  @Mapping(target = "paymentAmount",    source = "paymentAmount")
  @Mapping(target = "paymentSubject",   source = "paymentSubject")
  @Mapping(target = "executionDate",    source = "executionDate")
  @Mapping(target = "status",           ignore = true)
  @Mapping(target = "createdAt",        ignore = true)
  Payment mappingCreatePaymentInput2Payment(CreatePaymentInput createPaymentInput);

  @Mapping(target = "paymentReference", source = "paymentReference")
  CreatePaymentOutput mappingPayment2CreatePaymentOutput(Payment payment);

}
