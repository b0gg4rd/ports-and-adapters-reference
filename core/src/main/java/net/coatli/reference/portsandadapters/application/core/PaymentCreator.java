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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class PaymentCreator implements CreatePaymentPortIn {

  private final LoggingPortOut loggingPortOut;
  private final JsonTransformationPortOut jsonTransformationPortOut;
  private final CreatePaymentPortInMapper createPaymentPortInMapper;
  private final PaymentPersistencePortOut paymentPersistencePortOut;

  @Override
  public CreatePaymentOutput execute(final CreatePaymentInput createPaymentInput) {

    loggingPortOut.info(
      this.getClass(),
      "[core.payment.create] input: '{}'",
      jsonTransformationPortOut.toJson(createPaymentInput));

    return Optional
      .of(isNullCreatePaymentInput(createPaymentInput))
      .filter(validatePayerReference())
      .filter(validatePayeeReference())
      .filter(validatePaymentAmount())
      .filter(validateExecutionDate())
      .map(createPaymentPortInMapper::mappingCreatePaymentInput2Payment)
      .map(this::enrichPayment)
      .map(paymentPersistencePortOut::create)
      .map(createPaymentPortInMapper::mappingPayment2CreatePaymentOutput)
      .orElseThrow(() -> new IllegalStateException("The persistence create method return 'null'"));

  }

  private CreatePaymentInput isNullCreatePaymentInput(CreatePaymentInput createPaymentInput) {

    return Optional
      .ofNullable(createPaymentInput)
      .orElseThrow(() -> new PaymentInputException("Illegal argument, the 'createPaymentInput' must not be 'null'."));

  }

  private Predicate<CreatePaymentInput> validatePayerReference() {

    return createPaymentInput -> Optional
      .ofNullable(createPaymentInput.payerReference())
      .filter(Predicate.not(String::isBlank))
      .map(_ -> true)
      .orElseThrow(() -> new PaymentInputException("The value for 'payerReference' is required."));

  }

  private Predicate<CreatePaymentInput> validatePayeeReference() {

    return createPaymentInput -> Optional
      .ofNullable(createPaymentInput.payeeReference())
      .filter(Predicate.not(String::isBlank))
      .map(_ -> true)
      .orElseThrow(() -> new PaymentInputException("The value for 'payeeReference' is required."));

  }

  private Predicate<CreatePaymentInput> validatePaymentAmount() {

    return createPaymentInput -> {
      PaymentValidations.requirePositiveAmount(createPaymentInput.paymentAmount());
      return true;
    };

  }

  private Predicate<CreatePaymentInput> validateExecutionDate() {

    return createPaymentInput -> {
      PaymentValidations.requireFutureOrAbsentDate(createPaymentInput.executionDate());
      return true;
    };

  }

  private Payment enrichPayment(Payment payment) {

    return payment
      .setPaymentReference(UUID.randomUUID().toString())
      .setStatus(PaymentStatus.PENDING)
      .setExecutionDate(Optional.ofNullable(payment.getExecutionDate()).orElseGet(LocalDateTime::now))
      .setCreatedAt(LocalDateTime.now());

  }

}
