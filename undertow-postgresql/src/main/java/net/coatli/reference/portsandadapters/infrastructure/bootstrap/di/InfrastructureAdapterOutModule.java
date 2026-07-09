package net.coatli.reference.portsandadapters.infrastructure.bootstrap.di;

import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.logging.slf4j.Slf4jLoggingAdapter;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.PostgresqlPaymentPersistenceAdapter;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.MyBatisPaymentMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model.PaymentRow;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model.mapper.PostgresqlPaymentPersistenceMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model.mapper.PostgresqlPaymentPersistenceMapperImpl;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.transformation.avajejsonb.AvajeJsonbTransformationAdapter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

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
  public DataSource dataSource() {

    try (final var inputStream = InfrastructureAdapterOutModule.class.getResourceAsStream("/conf/hikari.properties")) {

      final var hikariConfig = new Properties();

      hikariConfig.load(inputStream);

      return new HikariDataSource(new HikariConfig(hikariConfig));

    } catch (final IOException ioException) {

      throw new RuntimeException("Error creating the data source", ioException);

    }

  }

  @Provides
  @Singleton
  public Configuration configuration(final DataSource dataSource) {

    LogFactory.useSlf4jLogging();

    final var configuration = new Configuration(
      new Environment(
        "production",
        new JdbcTransactionFactory(),
        dataSource));

    configuration.setMapUnderscoreToCamelCase(true);

    configuration
      .getTypeAliasRegistry()
      .registerAlias(
        PaymentRow.class.getSimpleName(),
        PaymentRow.class);

    configuration.addMapper(MyBatisPaymentMapper.class);

    return configuration;

  }

  @Provides
  @Singleton
  public SqlSessionFactory sqlSessionFactory(final Configuration configuration) {

    return new SqlSessionFactoryBuilder().build(configuration);

  }

  @Provides
  @Singleton
  public PostgresqlPaymentPersistenceMapper postgresqlPaymentPersistenceMapper() {

    return new PostgresqlPaymentPersistenceMapperImpl();

  }

  @Provides
  @Singleton
  public PaymentPersistencePortOut paymentPersistencePortOut(
      SqlSessionFactory                   sqlSessionFactory,
      PostgresqlPaymentPersistenceMapper  postgresqlPaymentPersistenceMapper,
      JsonTransformationPortOut           jsonTransformationPortOut,
      LoggingPortOut                      loggingPortOut) {

    return new PostgresqlPaymentPersistenceAdapter(
      sqlSessionFactory,
      postgresqlPaymentPersistenceMapper,
      jsonTransformationPortOut,
      loggingPortOut);

  }

}
