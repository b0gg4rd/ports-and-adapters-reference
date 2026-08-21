@Json.Import({
  CreatePaymentInput.class,
  CreatePaymentOutput.class,
  UpdatePaymentInput.class,
  UpdatePaymentOutput.class,
  DeletePaymentInput.class,
  DeletePaymentOutput.class,
  RetrieveAllPaymentsInput.class,
  RetrieveAllPaymentsOutput.class,
  Payment.class
})
package net.coatli.reference.portsandadapters.infrastructure.adapter.out.transformation.avajejsonb;

import io.avaje.jsonb.Json;
import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.CreatePaymentOutput;
import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.UpdatePaymentOutput;
import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentInput;
import net.coatli.reference.portsandadapters.application.port.in.model.DeletePaymentOutput;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsInput;
import net.coatli.reference.portsandadapters.application.port.in.model.RetrieveAllPaymentsOutput;
import net.coatli.reference.portsandadapters.domain.model.Payment;
