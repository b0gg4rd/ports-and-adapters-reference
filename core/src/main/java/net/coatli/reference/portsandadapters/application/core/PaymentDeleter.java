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

import java.util.Optional;
import java.util.function.Predicate;

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

    return Optional
      .of(isNullDeletePaymentInput(deletePaymentInput))
      .filter(validatePaymentReference())
      .map(input -> paymentPersistencePortOut
        .findByPaymentReference(input.paymentReference())
        .orElseThrow(() -> new PaymentNotFoundException(
          "Payment not found for paymentReference: " + input.paymentReference())))
      .flatMap(payment -> paymentPersistencePortOut.delete(payment.getPaymentReference()))
      .map(deletePaymentPortInMapper::mappingPayment2DeletePaymentOutput)
      .orElseThrow(() -> new IllegalStateException("The persistence delete method return 'null'"));

  }

  private DeletePaymentInput isNullDeletePaymentInput(DeletePaymentInput deletePaymentInput) {

    return Optional
      .ofNullable(deletePaymentInput)
      .orElseThrow(() -> new PaymentInputException("Illegal argument, the 'deletePaymentInput' must not be 'null'."));

  }

  private Predicate<DeletePaymentInput> validatePaymentReference() {

    return deletePaymentInput -> Optional
      .ofNullable(deletePaymentInput.paymentReference())
      .filter(Predicate.not(String::isBlank))
      .map(_ -> true)
      .orElseThrow(() -> new PaymentInputException("The value for 'paymentReference' is required."));

  }

}
