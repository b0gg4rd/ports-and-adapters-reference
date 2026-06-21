package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.UpdatePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentOutput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.UpdatePaymentPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentUpdater implements UpdatePaymentPortIn {

  private final LoggingPortOut loggingPortOut;
  private final JsonTransformationPortOut jsonTransformationPortOut;
  private final UpdatePaymentPortInMapper updatePaymentPortInMapper;
  private final PaymentPersistencePortOut paymentPersistencePortOut;

  @Override
  public UpdatePaymentOutput execute(final UpdatePaymentInput updatePaymentInput) {

    if (null == updatePaymentInput) {
      throw new PaymentInputException("Illegal argument, the 'updatePaymentInput' must not be 'null'.");
    }

    loggingPortOut.info(
      this.getClass(),
      "[core.payment.update] input: '{}'",
      jsonTransformationPortOut.toJson(updatePaymentInput));

    validateInput(updatePaymentInput);

    final var payment = paymentPersistencePortOut
      .findByPaymentReference(updatePaymentInput.paymentReference())
      .orElseThrow(() -> new PaymentNotFoundException(
        "Payment not found for paymentReference: " + updatePaymentInput.paymentReference()));

    applyUpdates(updatePaymentInput, payment);

    return updatePaymentPortInMapper.mappingPayment2UpdatePaymentOutput(
      paymentPersistencePortOut.update(payment));
  }

  private void validateInput(final UpdatePaymentInput updatePaymentInput) {

    if (null == updatePaymentInput.paymentReference() || updatePaymentInput.paymentReference().isBlank()) {
      throw new PaymentInputException("The value for 'paymentReference' is required.");
    }

    PaymentValidations.requireValidUUID(updatePaymentInput.paymentReference());
    PaymentValidations.requireFutureOrAbsentDate(updatePaymentInput.executionDate());
  }

  private void applyUpdates(final UpdatePaymentInput updatePaymentInput, final Payment payment) {

    if (null != updatePaymentInput.paymentAmount()) {
      PaymentValidations.requirePositiveAmount(updatePaymentInput.paymentAmount());
      payment.setPaymentAmount(updatePaymentInput.paymentAmount());
    }

    if (null != updatePaymentInput.paymentSubject() && !updatePaymentInput.paymentSubject().isBlank()) {
      payment.setPaymentSubject(updatePaymentInput.paymentSubject());
    }

    if (null != updatePaymentInput.executionDate()) {
      PaymentValidations.requireFutureOrAbsentDate(updatePaymentInput.executionDate());
      payment.setExecutionDate(updatePaymentInput.executionDate());
    }
  }
}
