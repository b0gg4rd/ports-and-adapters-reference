package net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.domain.model.Page;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.MyBatisPaymentMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model.mapper.PostgresqlPaymentPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Optional;

@RequiredArgsConstructor
public class PostgresqlPaymentPersistenceAdapter implements PaymentPersistencePortOut {

  private static final int SUCCESS_INSERT = 1;

  private static final int SUCCESS_UPDATE = 1;

  private static final int SUCCESS_DELETE = 1;

  private static final int NOT_FOUND_DELETE = 0;

  private final SqlSessionFactory sqlSessionFactory;

  private final PostgresqlPaymentPersistenceMapper postgresqlPaymentPersistenceMapper;

  private final JsonTransformationPortOut jsonTransformationPortOut;

  private final LoggingPortOut loggingPortOut;

  @Override
  public Payment create(final Payment payment) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.create] input: '{}'",
      jsonTransformationPortOut.toJson(payment));

    try (final var sqlSession = sqlSessionFactory.openSession()) {

      final var rows = sqlSession
        .getMapper(MyBatisPaymentMapper.class)
        .insert(postgresqlPaymentPersistenceMapper.mappingPayment2PaymentRow(payment));

      if (SUCCESS_INSERT == rows) {

        sqlSession.commit();

        loggingPortOut.info(
          this.getClass(),
          "[persistence.payment.create] commited");

      } else {

        sqlSession.rollback();

        loggingPortOut.error(
          this.getClass(),
          "[persistence.payment.create] rollback: '{}'", rows);

        throw new RuntimeException(String.format("Error creating payment, insert result '%s'", rows));

      }

    }

    return payment;

  }

  @Override
  public Page<Payment> retrieveAll(final Page<Payment> page) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.retrieve-all] input: '{}'",
      jsonTransformationPortOut.toJson(page.getPagination()));

    try (final var sqlSession = sqlSessionFactory.openSession(true)) {

      final var mapper = sqlSession.getMapper(MyBatisPaymentMapper.class);

      final var totalElements = mapper.count();

      return page
        .setContent(
          mapper
            .selectAll(
              page.getPagination().getSize(),
              page.getPagination().getPage() * page.getPagination().getSize())
            .stream()
            .map(postgresqlPaymentPersistenceMapper::mappingPaymentRow2Payment)
            .toList())
        .setTotalElements(totalElements)
        .setTotalPages((int) Math.ceil((double) totalElements / page.getPagination().getSize()));

    }

  }

  @Override
  public Optional<Payment> findByPaymentReference(final String paymentReference) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.find] input: '{}'",
      paymentReference);

    try (final var sqlSession = sqlSessionFactory.openSession(true)) {

      return Optional
        .ofNullable(sqlSession
          .getMapper(MyBatisPaymentMapper.class)
          .selectById(paymentReference))
        .map(postgresqlPaymentPersistenceMapper::mappingPaymentRow2Payment);

    }

  }

  @Override
  public Payment update(final Payment payment) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.update] input: '{}'",
      jsonTransformationPortOut.toJson(payment));

    try (final var sqlSession = sqlSessionFactory.openSession()) {

      final var rows = sqlSession
        .getMapper(MyBatisPaymentMapper.class)
        .update(postgresqlPaymentPersistenceMapper.mappingPayment2PaymentRow(payment));

      if (SUCCESS_UPDATE == rows) {

        sqlSession.commit();

        loggingPortOut.info(
          this.getClass(),
          "[persistence.payment.update] commited");

        return payment;

      } else {

        sqlSession.rollback();

        loggingPortOut.error(
          this.getClass(),
          "[persistence.payment.update] rollback: '{}'", rows);

        throw new RuntimeException(String.format("Error updating payment, update result '%s'", rows));

      }

    }

  }

  @Override
  public Payment delete(final String paymentReference) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.delete] input: '{}'",
      paymentReference);

    try (final var sqlSession = sqlSessionFactory.openSession(true)) {

      final var rows = sqlSession
        .getMapper(MyBatisPaymentMapper.class)
        .delete(paymentReference);

      if (NOT_FOUND_DELETE == rows) {

        loggingPortOut.info(
          this.getClass(),
          "[persistence.payment.delete] not found");

        throw new PaymentNotFoundException(paymentReference);

      } else if (SUCCESS_DELETE == rows) {

        sqlSession.commit();

        loggingPortOut.info(
          this.getClass(),
          "[persistence.payment.delete] commited");

        return new Payment().setPaymentReference(paymentReference);

      } else {

        sqlSession.rollback();

        loggingPortOut.error(
          this.getClass(),
          "[persistence.payment.delete] rollback: '{}'", rows);

        throw new RuntimeException(String.format("Error deleting payment, delete result '%s'", rows));

      }

    }

  }

}
