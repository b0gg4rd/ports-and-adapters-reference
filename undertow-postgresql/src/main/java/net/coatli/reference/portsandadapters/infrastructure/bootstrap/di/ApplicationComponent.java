package net.coatli.reference.portsandadapters.infrastructure.bootstrap.di;

import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.CreatePaymentHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.DeletePaymentHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.RetrieveAllPaymentsHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.UpdatePaymentHandler;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationCoreModule.class})
public interface ApplicationComponent {

  LoggingPortOut loggingPortOut();

  CreatePaymentHandler createPaymentHandler();

  RetrieveAllPaymentsHandler retrieveAllPaymentsHandler();

  UpdatePaymentHandler updatePaymentHandler();

  DeletePaymentHandler deletePaymentHandler();

}