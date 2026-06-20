package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.RetrieveAllPaymentsPortIn;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsOutput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.RetrieveAllPaymentsPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentAllRetriever implements RetrieveAllPaymentsPortIn {

  private final LoggingPortOut loggingPortOut;
  private final JsonTransformationPortOut jsonTransformationPortOut;
  private final RetrieveAllPaymentsPortInMapper retrieveAllPaymentsPortInMapper;
  private final PaymentPersistencePortOut paymentPersistencePortOut;

  @Override
  public RetrieveAllPaymentsOutput execute(final RetrieveAllPaymentsInput retrieveAllPaymentsInput) {

    loggingPortOut.info(
      this.getClass(),
      "[core.payment.retrieve] input: '{}'",
      jsonTransformationPortOut.toJson(retrieveAllPaymentsInput));

    if (null == retrieveAllPaymentsInput) {
      throw new PaymentInputException("Illegal argument, the 'retrieveAllPaymentsInput' must not be 'null'.");
    }

    return retrieveAllPaymentsPortInMapper.mappingPayments2RetrieveAllPaymentsOutput(
      paymentPersistencePortOut.retrieveAll(
        retrieveAllPaymentsPortInMapper.mappingRetrieveAllPaymentsInput2Page(retrieveAllPaymentsInput)));
  }
}
