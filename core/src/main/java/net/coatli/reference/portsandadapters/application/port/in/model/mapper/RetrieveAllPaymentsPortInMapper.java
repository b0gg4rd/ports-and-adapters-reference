package net.coatli.reference.portsandadapters.application.port.in.model.mapper;

import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsOutput;
import net.coatli.reference.portsandadapters.domain.model.Page;
import net.coatli.reference.portsandadapters.domain.model.Pagination;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import org.mapstruct.Mapper;

@Mapper
public interface RetrieveAllPaymentsPortInMapper {

  default Page<Payment> mappingRetrieveAllPaymentsInput2Page(RetrieveAllPaymentsInput input) {
    return new Page<Payment>()
      .setPagination(
        new Pagination()
          .setPage(input.page())
          .setSize(input.size()));
  }

  default RetrieveAllPaymentsOutput mappingPayments2RetrieveAllPaymentsOutput(Page<Payment> payments) {
    return new RetrieveAllPaymentsOutput(payments);
  }

}
