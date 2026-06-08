package net.coatli.reference.portsandadapters.application.port.in;

import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentOutput;

public interface UpdatePaymentPortIn {

  UpdatePaymentOutput execute(UpdatePaymentInput updatePaymentInput);

}
