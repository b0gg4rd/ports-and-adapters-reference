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

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class PaymentUpdater implements UpdatePaymentPortIn {

  private final LoggingPortOut loggingPortOut;
  private final JsonTransformationPortOut jsonTransformationPortOut;
  private final UpdatePaymentPortInMapper updatePaymentPortInMapper;
  private final PaymentPersistencePortOut paymentPersistencePortOut;

  @Override
  public UpdatePaymentOutput execute(final UpdatePaymentInput updatePaymentInput) {

    loggingPortOut.info(
      this.getClass(),
      "[core.payment.update] input: '{}'",
      jsonTransformationPortOut.toJson(updatePaymentInput));

    return Optional
      .of(isNullUpdatePaymentInput(updatePaymentInput))
      .filter(validatePayeeReference())
      .filter(validateExecutionDate())
      .map(findByPaymentReference())
      .map(applyUpdates(updatePaymentInput))
      .flatMap(paymentPersistencePortOut::update)
      .map(updatePaymentPortInMapper::mappingPayment2UpdatePaymentOutput)
      .orElseThrow(() -> new IllegalStateException("The persistence update method return 'null'"));

  }

  private UpdatePaymentInput isNullUpdatePaymentInput(UpdatePaymentInput updatePaymentInput) {

    return Optional
      .ofNullable(updatePaymentInput)
      .orElseThrow(() -> new PaymentInputException("Illegal argument, the 'updatePaymentInput' must not be 'null'."));

  }

  private Function<UpdatePaymentInput, Payment> findByPaymentReference() {

    return updatePaymentInput -> paymentPersistencePortOut
      .findByPaymentReference(updatePaymentInput.payeeReference())
      .orElseThrow(() -> new PaymentNotFoundException("Payment not found for payeeReference: " + updatePaymentInput.payeeReference()));

  }

  private Function<Payment, Payment> applyUpdates(UpdatePaymentInput updatePaymentInput) {

    return payment -> {

      Optional.ofNullable(updatePaymentInput.paymentAmount())
        .ifPresent(amount -> {
          PaymentValidations.requirePositiveAmount(amount);
          payment.setPaymentAmount(amount);
        });

      Optional.ofNullable(updatePaymentInput.paymentSubject())
        .filter(Predicate.not(String::isBlank))
        .ifPresent(payment::setPaymentSubject);

      Optional.ofNullable(updatePaymentInput.executionDate())
        .ifPresent(date -> {
          PaymentValidations.requireFutureOrAbsentDate(date);
          payment.setExecutionDate(date);
        });

      return payment;

    };

  }

  private Predicate<UpdatePaymentInput> validatePayeeReference() {

    return updatePaymentInput -> Optional
      .ofNullable(updatePaymentInput.payeeReference())
      .filter(Predicate.not(String::isBlank))
      .map(_ -> true)
      .orElseThrow(() -> new PaymentInputException("The value for 'payeeReference' is required."));

  }

  private Predicate<UpdatePaymentInput> validateExecutionDate() {

    return updatePaymentInput -> {
      PaymentValidations.requireFutureOrAbsentDate(updatePaymentInput.executionDate());
      return true;
    };

  }

}
