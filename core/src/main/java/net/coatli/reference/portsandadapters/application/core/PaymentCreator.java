package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.CreatePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentOutput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.CreatePaymentPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.domain.enums.PaymentStatus;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class PaymentCreator implements CreatePaymentPortIn {

  private final LoggingPortOut loggingPortOut;
  private final JsonTransformationPortOut jsonTransformationPortOut;
  private final CreatePaymentPortInMapper createPaymentPortInMapper;
  private final PaymentPersistencePortOut paymentPersistencePortOut;

  @Override
  public CreatePaymentOutput execute(final CreatePaymentInput createPaymentInput) {

    if (null == createPaymentInput) {
      throw new PaymentInputException("Illegal argument, the 'createPaymentInput' must not be 'null'.");
    }

    loggingPortOut.info(
      this.getClass(),
      "[core.payment.create] input: '{}'",
      jsonTransformationPortOut.toJson(createPaymentInput));

    validateInput(createPaymentInput);

    final var persisted = paymentPersistencePortOut.create(
      enrichPayment(createPaymentPortInMapper.mappingCreatePaymentInput2Payment(createPaymentInput)));

    if (null == persisted) {
      throw new IllegalStateException("The persistence create method return 'null'");
    }

    return createPaymentPortInMapper.mappingPayment2CreatePaymentOutput(persisted);
  }

  private void validateInput(final CreatePaymentInput createPaymentInput) {

    if (null == createPaymentInput.payerReference() || createPaymentInput.payerReference().isBlank()) {
      throw new PaymentInputException("The value for 'payerReference' is required.");
    }

    if (null == createPaymentInput.payeeReference() || createPaymentInput.payeeReference().isBlank()) {
      throw new PaymentInputException("The value for 'paymentReference' is required.");
    }

    PaymentValidations.requirePositiveAmount(createPaymentInput.paymentAmount());
    PaymentValidations.requireFutureOrAbsentDate(createPaymentInput.executionDate());
  }

  private Payment enrichPayment(final Payment payment) {

    payment.setPaymentReference(UUID.randomUUID().toString());
    payment.setStatus(PaymentStatus.PENDING);
    payment.setCreatedAt(LocalDateTime.now());

    if (null == payment.getExecutionDate()) {
      payment.setExecutionDate(LocalDateTime.now());
    }

    return payment;
  }
}
