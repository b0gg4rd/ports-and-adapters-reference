package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.DeletePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentOutput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.DeletePaymentPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentDeleter implements DeletePaymentPortIn {

  private final LoggingPortOut loggingPortOut;
  private final JsonTransformationPortOut jsonTransformationPortOut;
  private final DeletePaymentPortInMapper deletePaymentPortInMapper;
  private final PaymentPersistencePortOut paymentPersistencePortOut;

  @Override
  public DeletePaymentOutput execute(final DeletePaymentInput deletePaymentInput) {

    loggingPortOut.info(
      this.getClass(),
      "[core.payment.delete] input: '{}'",
      jsonTransformationPortOut.toJson(deletePaymentInput));

    validateInput(deletePaymentInput);

    final var payment = paymentPersistencePortOut
      .findByPaymentReference(deletePaymentInput.paymentReference())
      .orElseThrow(() -> new PaymentNotFoundException(
        "Payment not found for paymentReference: " + deletePaymentInput.paymentReference()));

    return deletePaymentPortInMapper.mappingPayment2DeletePaymentOutput(
      paymentPersistencePortOut.delete(payment.getPaymentReference()));
  }

  private void validateInput(final DeletePaymentInput deletePaymentInput) {

    if (null == deletePaymentInput) {
      throw new PaymentInputException("Illegal argument, the 'deletePaymentInput' must not be 'null'.");
    }

    if (null == deletePaymentInput.paymentReference() || deletePaymentInput.paymentReference().isBlank()) {
      throw new PaymentInputException("The value for 'paymentReference' is required.");
    }

    PaymentValidations.requireValidUUID(deletePaymentInput.paymentReference());
  }
}
