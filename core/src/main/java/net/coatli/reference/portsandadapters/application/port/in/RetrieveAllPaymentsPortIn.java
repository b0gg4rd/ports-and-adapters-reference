package net.coatli.reference.portsandadapters.application.port.in;

import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsOutput;

public interface RetrieveAllPaymentsPortIn {

  RetrieveAllPaymentsOutput execute(RetrieveAllPaymentsInput retrieveAllPaymentsInput);

}
