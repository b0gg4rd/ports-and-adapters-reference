package net.coatli.reference.portsandadapters.infrastructure.bootstrap.di;

import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.CreatePaymentHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.CreatePaymentHandlerMapperImpl;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.DeletePaymentHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.DeletePaymentHandlerMapperImpl;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.RetrieveAllPaymentsHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.RetrieveAllPaymentsHandlerMapperImpl;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.UpdatePaymentHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.UpdatePaymentHandlerMapperImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class InfrastructureAdapterInModule {

  @Provides
  @Singleton
  public CreatePaymentHandlerMapper createPaymentHandlerMapper() {

    return new CreatePaymentHandlerMapperImpl();

  }

  @Provides
  @Singleton
  public RetrieveAllPaymentsHandlerMapper retrieveAllPaymentsHandlerMapper() {

    return new RetrieveAllPaymentsHandlerMapperImpl();

  }

  @Provides
  @Singleton
  public UpdatePaymentHandlerMapper updatePaymentHandlerMapper() {

    return new UpdatePaymentHandlerMapperImpl();

  }

  @Provides
  @Singleton
  public DeletePaymentHandlerMapper deletePaymentHandlerMapper() {

    return new DeletePaymentHandlerMapperImpl();

  }

}
