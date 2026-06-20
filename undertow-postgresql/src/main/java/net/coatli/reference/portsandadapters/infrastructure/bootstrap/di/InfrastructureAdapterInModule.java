package net.coatli.reference.portsandadapters.infrastructure.bootstrap.di;

import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.CreatePaymentHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.DeletePaymentHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.RetrieveAllPaymentsHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.UpdatePaymentHandlerMapper;
import org.codejargon.feather.Provides;
import org.mapstruct.factory.Mappers;

import javax.inject.Singleton;

public class InfrastructureAdapterInModule {

  @Provides
  @Singleton
  public CreatePaymentHandlerMapper createPaymentHandlerMapper() {

    return Mappers.getMapper(CreatePaymentHandlerMapper.class);

  }

  @Provides
  @Singleton
  public RetrieveAllPaymentsHandlerMapper retrieveAllPaymentsHandlerMapper() {

    return Mappers.getMapper(RetrieveAllPaymentsHandlerMapper.class);

  }

  @Provides
  @Singleton
  public UpdatePaymentHandlerMapper updatePaymentHandlerMapper() {

    return Mappers.getMapper(UpdatePaymentHandlerMapper.class);

  }

  @Provides
  @Singleton
  public DeletePaymentHandlerMapper deletePaymentHandlerMapper() {

    return Mappers.getMapper(DeletePaymentHandlerMapper.class);

  }

}
