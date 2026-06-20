package net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model.mapper;

import net.coatli.reference.portsandadapters.domain.enums.PaymentStatus;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model.PaymentRow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostgresqlPaymentPersistenceMapper {

  @Mapping(target = "id",            source = "payment.paymentReference")
  @Mapping(target = "payerId",       source = "payment.payerReference")
  @Mapping(target = "payeeId",       source = "payment.payeeReference")
  @Mapping(target = "amount",        source = "payment.paymentAmount")
  @Mapping(target = "subject",       source = "payment.paymentSubject")
  @Mapping(target = "executionDate", source = "payment.executionDate")
  @Mapping(target = "status",        expression = "java(payment.getStatus() != null ? payment.getStatus().name() : null)")
  @Mapping(target = "createdAt",     source = "payment.createdAt")
  PaymentRow mappingPayment2PaymentRow(Payment payment);

  @Mapping(target = "paymentReference", source = "paymentRow.id")
  @Mapping(target = "payerReference",   source = "paymentRow.payerId")
  @Mapping(target = "payeeReference",   source = "paymentRow.payeeId")
  @Mapping(target = "paymentAmount",    source = "paymentRow.amount")
  @Mapping(target = "paymentSubject",   source = "paymentRow.subject")
  @Mapping(target = "executionDate",    source = "paymentRow.executionDate")
  @Mapping(target = "status",           expression = "java(paymentRow.status() != null ? net.coatli.reference.portsandadapters.domain.enums.PaymentStatus.valueOf(paymentRow.status()) : null)")
  @Mapping(target = "createdAt",        source = "paymentRow.createdAt")
  Payment mappingPaymentRow2Payment(PaymentRow paymentRow);

}
