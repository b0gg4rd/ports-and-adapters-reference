package net.coatli.reference.portsandadapters.application.port.in;

import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentOutput;

public interface DeletePaymentPortIn {

  DeletePaymentOutput execute(DeletePaymentInput deletePaymentInput);

}
