package net.coatli.reference.portsandadapters.infrastructure.bootstrap.di;

import net.coatli.reference.portsandadapters.application.core.PaymentAllRetriever;
import net.coatli.reference.portsandadapters.application.core.PaymentCreator;
import net.coatli.reference.portsandadapters.application.core.PaymentDeleter;
import net.coatli.reference.portsandadapters.application.core.PaymentUpdater;
import net.coatli.reference.portsandadapters.application.port.in.CreatePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.in.DeletePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.in.RetrieveAllPaymentsPortIn;
import net.coatli.reference.portsandadapters.application.port.in.UpdatePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.CreatePaymentPortInMapper;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.CreatePaymentPortInMapperImpl;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.DeletePaymentPortInMapper;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.DeletePaymentPortInMapperImpl;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.RetrieveAllPaymentsPortInMapper;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.RetrieveAllPaymentsPortInMapperImpl;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.UpdatePaymentPortInMapper;
import net.coatli.reference.portsandadapters.application.port.in.model.mapper.UpdatePaymentPortInMapperImpl;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(includes = {InfrastructureAdapterOutModule.class, InfrastructureAdapterInModule.class})
public class ApplicationCoreModule {

  @Provides
  @Singleton
  public CreatePaymentPortInMapper createPaymentPortInMapper() {

    return new CreatePaymentPortInMapperImpl();

  }

  @Provides
  @Singleton
  public RetrieveAllPaymentsPortInMapper retrieveAllPaymentsPortInMapper() {

    return new RetrieveAllPaymentsPortInMapperImpl();

  }

  @Provides
  @Singleton
  public UpdatePaymentPortInMapper updatePaymentPortInMapper() {

    return new UpdatePaymentPortInMapperImpl();

  }

  @Provides
  @Singleton
  public DeletePaymentPortInMapper deletePaymentPortInMapper() {

    return new DeletePaymentPortInMapperImpl();

  }

  @Provides
  @Singleton
  public CreatePaymentPortIn createPaymentPortIn(
      LoggingPortOut              loggingPortOut,
      JsonTransformationPortOut   jsonTransformationPortOut,
      CreatePaymentPortInMapper   createPaymentPortInMapper,
      PaymentPersistencePortOut   paymentPersistencePortOut) {

    return new PaymentCreator(
      loggingPortOut,
      jsonTransformationPortOut,
      createPaymentPortInMapper,
      paymentPersistencePortOut);

  }

  @Provides
  @Singleton
  public RetrieveAllPaymentsPortIn retrieveAllPaymentsPortIn(
      LoggingPortOut                  loggingPortOut,
      JsonTransformationPortOut       jsonTransformationPortOut,
      RetrieveAllPaymentsPortInMapper retrieveAllPaymentsPortInMapper,
      PaymentPersistencePortOut       paymentPersistencePortOut) {

    return new PaymentAllRetriever(
      loggingPortOut,
      jsonTransformationPortOut,
      retrieveAllPaymentsPortInMapper,
      paymentPersistencePortOut);

  }

  @Provides
  @Singleton
  public UpdatePaymentPortIn updatePaymentPortIn(
      LoggingPortOut              loggingPortOut,
      JsonTransformationPortOut   jsonTransformationPortOut,
      UpdatePaymentPortInMapper   updatePaymentPortInMapper,
      PaymentPersistencePortOut   paymentPersistencePortOut) {

    return new PaymentUpdater(
      loggingPortOut,
      jsonTransformationPortOut,
      updatePaymentPortInMapper,
      paymentPersistencePortOut);

  }

  @Provides
  @Singleton
  public DeletePaymentPortIn deletePaymentPortIn(
      LoggingPortOut              loggingPortOut,
      JsonTransformationPortOut   jsonTransformationPortOut,
      DeletePaymentPortInMapper   deletePaymentPortInMapper,
      PaymentPersistencePortOut   paymentPersistencePortOut) {

    return new PaymentDeleter(
      loggingPortOut,
      jsonTransformationPortOut,
      deletePaymentPortInMapper,
      paymentPersistencePortOut);

  }

}
