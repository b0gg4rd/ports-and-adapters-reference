package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.RetrieveAllPaymentsPortIn;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsOutput;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.RetrieveAllPaymentsPortInMapper;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.domain.model.Page;
import net.coatli.reference.portsandadapters.domain.model.Pagination;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

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

    return Optional
      .ofNullable(retrieveAllPaymentsInput)
      .map(parsingPagination())
      .map(page -> paymentPersistencePortOut.retrieveAll(page))
      .map(retrieveAllPaymentsPortInMapper::mappingPayments2RetrieveAllPaymentsOutput)
      .orElseThrow();

  }

  private Function<RetrieveAllPaymentsInput, Page<Payment>> parsingPagination() {

    return retrieveAllPaymentsInput -> new Page<Payment>()
      .setPagination(
        new Pagination()
          .setPage(retrieveAllPaymentsInput.page())
          .setSize(retrieveAllPaymentsInput.size()));

  }

}
