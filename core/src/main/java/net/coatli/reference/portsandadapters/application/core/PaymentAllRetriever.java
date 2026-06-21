package net.coatli.reference.portsandadapters.application.core;

import net.coatli.reference.portsandadapters.application.port.in.RetrieveAllPaymentsPortIn;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaginationInvalidException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
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

@RequiredArgsConstructor
public class PaymentAllRetriever implements RetrieveAllPaymentsPortIn {

  private final LoggingPortOut loggingPortOut;
  private final JsonTransformationPortOut jsonTransformationPortOut;
  private final RetrieveAllPaymentsPortInMapper retrieveAllPaymentsPortInMapper;
  private final PaymentPersistencePortOut paymentPersistencePortOut;

  @Override
  public RetrieveAllPaymentsOutput execute(final RetrieveAllPaymentsInput retrieveAllPaymentsInput) {

    if (null == retrieveAllPaymentsInput) {
      throw new PaymentInputException("Illegal argument, the 'retrieveAllPaymentsInput' must not be 'null'.");
    }

    loggingPortOut.info(
      this.getClass(),
      "[core.payment.retrieve] input: '{}'",
      jsonTransformationPortOut.toJson(retrieveAllPaymentsInput));

    return retrieveAllPaymentsPortInMapper.mappingPayments2RetrieveAllPaymentsOutput(
      paymentPersistencePortOut.retrieveAll(
        parsePagination(retrieveAllPaymentsInput)));
  }

  private Page<Payment> parsePagination(final RetrieveAllPaymentsInput retrieveAllPaymentsInput) {

    try {

      var page = Optional
        .ofNullable(retrieveAllPaymentsInput.page())
        .map(Integer::parseInt)
        .orElse(0);

      var size = Optional
        .ofNullable(retrieveAllPaymentsInput.size())
        .map(Integer::parseInt)
        .orElse(10);

      return new Page<Payment>().setPagination(new Pagination().setPage(page).setSize(size));

    } catch (NumberFormatException exc) {

      loggingPortOut.error(this.getClass(), "Error parsing pagination", exc);
      throw new PaginationInvalidException("Illegal argument, invalid pagination parameters.", exc);

    }

  }

}
