package net.coatli.reference.portsandadapters.application.port.in.model;

import net.coatli.reference.portsandadapters.domain.model.Page;
import net.coatli.reference.portsandadapters.domain.model.Payment;

public record RetrieveAllPaymentsOutput(

  Page<Payment> payments) {

}
