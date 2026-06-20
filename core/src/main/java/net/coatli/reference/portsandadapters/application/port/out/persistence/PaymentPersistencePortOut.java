package net.coatli.reference.portsandadapters.application.port.out.persistence;

import net.coatli.reference.portsandadapters.domain.model.Page;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import java.util.Optional;

public interface PaymentPersistencePortOut {

  Payment create(Payment payment);

  Page<Payment> retrieveAll(Page<Payment> page);

  Optional<Payment> findByPaymentReference(String paymentReference);

  Payment update(Payment payment);

  Payment delete(String paymentReference);

}
