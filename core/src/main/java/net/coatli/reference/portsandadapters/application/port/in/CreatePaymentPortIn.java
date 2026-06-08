package net.coatli.reference.portsandadapters.application.port.in;

import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentOutput;

public interface CreatePaymentPortIn {

  CreatePaymentOutput execute(CreatePaymentInput createPaymentInput);

}
