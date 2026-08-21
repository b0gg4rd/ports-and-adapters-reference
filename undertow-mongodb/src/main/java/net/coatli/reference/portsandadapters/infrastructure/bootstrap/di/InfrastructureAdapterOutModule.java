package net.coatli.reference.portsandadapters.infrastructure.bootstrap.di;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.logging.slf4j.Slf4jLoggingAdapter;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb.MongoDbPaymentPersistenceAdapter;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb.model.mapper.MongoDbPaymentPersistenceMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb.model.mapper.MongoDbPaymentPersistenceMapperImpl;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.transformation.avajejsonb.AvajeJsonbTransformationAdapter;
import net.coatli.reference.portsandadapters.infrastructure.bootstrap.ApplicationProperties;
import dagger.Module;
import dagger.Provides;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.jsr310.Jsr310CodecProvider;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.inject.Singleton;

@Module
public class InfrastructureAdapterOutModule {

  @Provides
  @Singleton
  public JsonTransformationPortOut jsonTransformationPortOut() {

    return new AvajeJsonbTransformationAdapter();

  }

  @Provides
  @Singleton
  public LoggingPortOut loggingPortOut() {

    return new Slf4jLoggingAdapter();

  }

  @Provides
  @Singleton
  public MongoClient mongoClient() {

    final var codecRegistry = CodecRegistries.fromRegistries(
      MongoClientSettings.getDefaultCodecRegistry(),
      CodecRegistries.fromProviders(
        new Jsr310CodecProvider(),
        PojoCodecProvider.builder().automatic(true).build()));

    final var settings = MongoClientSettings.builder()
      .applyConnectionString(new ConnectionString(ApplicationProperties.APPLICATION_PROPERTIES.get("database.url")))
      .credential(
        MongoCredential.createCredential(
          ApplicationProperties.APPLICATION_PROPERTIES.get("database.username"),
          "admin",
          ApplicationProperties.APPLICATION_PROPERTIES.get("database.password").toCharArray()))
      .codecRegistry(codecRegistry)
      .build();

    return MongoClients.create(settings);

  }

  @Provides
  @Singleton
  public MongoDatabase mongoDatabase(final MongoClient mongoClient) {

    return mongoClient.getDatabase(new ConnectionString(ApplicationProperties.APPLICATION_PROPERTIES.get("database.url")).getDatabase());

  }

  @Provides
  @Singleton
  public MongoDbPaymentPersistenceMapper mongoDbPaymentPersistenceMapper() {

    return new MongoDbPaymentPersistenceMapperImpl();

  }

  @Provides
  @Singleton
  public PaymentPersistencePortOut paymentPersistencePortOut(
      MongoDatabase                       mongoDatabase,
      MongoDbPaymentPersistenceMapper     mongoDbPaymentPersistenceMapper,
      JsonTransformationPortOut           jsonTransformationPortOut,
      LoggingPortOut                      loggingPortOut) {

    return new MongoDbPaymentPersistenceAdapter(
      mongoDatabase,
      mongoDbPaymentPersistenceMapper,
      jsonTransformationPortOut,
      loggingPortOut);

  }

}
