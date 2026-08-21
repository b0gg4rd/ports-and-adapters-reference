package net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb.model.mapper;

import net.coatli.reference.portsandadapters.domain.enums.PaymentStatus;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb.model.PaymentDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MongoDbPaymentPersistenceMapper {

  @Mapping(target = "id",            source = "payment.paymentReference")
  @Mapping(target = "payerId",       source = "payment.payerReference")
  @Mapping(target = "payeeId",       source = "payment.payeeReference")
  @Mapping(target = "amount",        source = "payment.paymentAmount")
  @Mapping(target = "subject",       source = "payment.paymentSubject")
  @Mapping(target = "executionDate", source = "payment.executionDate")
  @Mapping(target = "createdAt",     source = "payment.createdAt")
  PaymentDocument mappingPayment2PaymentDocument(Payment payment);

  @Mapping(target = "paymentReference", source = "paymentDocument.id")
  @Mapping(target = "payerReference",   source = "paymentDocument.payerId")
  @Mapping(target = "payeeReference",   source = "paymentDocument.payeeId")
  @Mapping(target = "paymentAmount",    source = "paymentDocument.amount")
  @Mapping(target = "paymentSubject",   source = "paymentDocument.subject")
  @Mapping(target = "executionDate",    source = "paymentDocument.executionDate")
  @Mapping(target = "createdAt",        source = "paymentDocument.createdAt")
  Payment mappingPaymentDocument2Payment(PaymentDocument paymentDocument);

  default String mappingPaymentStatus2String(final PaymentStatus paymentStatus) {
    return paymentStatus != null ? paymentStatus.name() : null;
  }

  default PaymentStatus mappingString2PaymentStatus(final String status) {
    return status != null ? PaymentStatus.findByName(status) : null;
  }

}
