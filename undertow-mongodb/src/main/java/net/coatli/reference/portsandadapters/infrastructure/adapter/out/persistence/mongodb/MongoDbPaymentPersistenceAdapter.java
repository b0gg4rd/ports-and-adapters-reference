package net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.PaymentPersistencePortOut;
import net.coatli.reference.portsandadapters.application.port.out.persistence.exception.PaymentPersistenceException;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.domain.model.Page;
import net.coatli.reference.portsandadapters.domain.model.Payment;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb.model.PaymentDocument;
import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.mongodb.model.mapper.MongoDbPaymentPersistenceMapper;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
public class MongoDbPaymentPersistenceAdapter implements PaymentPersistencePortOut {

  private static final String COLLECTION_NAME = "payment";

  private final MongoDatabase mongoDatabase;

  private final MongoDbPaymentPersistenceMapper mongoDbPaymentPersistenceMapper;

  private final JsonTransformationPortOut jsonTransformationPortOut;

  private final LoggingPortOut loggingPortOut;

  private MongoCollection<PaymentDocument> collection() {

    return mongoDatabase.getCollection(COLLECTION_NAME, PaymentDocument.class);

  }

  @Override
  public Payment create(final Payment payment) {

    final var paymentDocument = mongoDbPaymentPersistenceMapper.mappingPayment2PaymentDocument(payment);

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.create] input: '{}'",
      jsonTransformationPortOut.toJson(paymentDocument));

    try {

      collection().insertOne(paymentDocument);

      loggingPortOut.info(this.getClass(), "[persistence.payment.create] commited");

    } catch (final Exception exception) {

      loggingPortOut.error(this.getClass(), "[persistence.payment.create] failed: " + exception.getMessage());

      throw new PaymentPersistenceException(String.format("Error creating payment: '%s'", exception.getMessage()));

    }

    return payment;

  }

  @Override
  public Page<Payment> retrieveAll(final Page<Payment> page) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.retrieve-all] page '{}' size '{}'",
      page.getPagination().getPage(),
      page.getPagination().getSize());

    final var totalElements = collection().countDocuments();

    return page
      .setContent(
        collection()
          .find()
          .sort(Sorts.descending("created_at"))
          .skip(page.getPagination().getPage() * page.getPagination().getSize())
          .limit(page.getPagination().getSize())
          .map(mongoDbPaymentPersistenceMapper::mappingPaymentDocument2Payment)
          .into(new ArrayList<>()))
      .setTotalElements(totalElements)
      .setTotalPages((int) Math.ceil((double) totalElements / page.getPagination().getSize()));

  }

  @Override
  public Optional<Payment> findByPaymentReference(final String paymentReference) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.find] input: '{}'",
      paymentReference);

    return Optional
      .ofNullable(collection().find(Filters.eq("_id", paymentReference)).first())
      .map(mongoDbPaymentPersistenceMapper::mappingPaymentDocument2Payment);

  }

  @Override
  public Payment update(final Payment payment) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.update] input: '{}'",
      jsonTransformationPortOut.toJson(payment));

    final var updated = collection()
      .findOneAndReplace(
        Filters.eq("_id", payment.getPaymentReference()),
        mongoDbPaymentPersistenceMapper.mappingPayment2PaymentDocument(payment));

    if (updated == null) {

      loggingPortOut.error(this.getClass(), "[persistence.payment.update] not found: '{}'", payment.getPaymentReference());

      throw new PaymentPersistenceException(
        String.format("Error updating payment, payment not found: '%s'", payment.getPaymentReference()));

    }

    loggingPortOut.info(this.getClass(), "[persistence.payment.update] commited");

    return payment;

  }

  @Override
  public Payment delete(final String paymentReference) {

    loggingPortOut.info(
      this.getClass(),
      "[persistence.payment.delete] input: '{}'",
      paymentReference);

    final var deleted = collection().findOneAndDelete(Filters.eq("_id", paymentReference));

    if (deleted == null) {

      loggingPortOut.error(this.getClass(), "[persistence.payment.delete] not found: '{}'", paymentReference);

      throw new PaymentPersistenceException(
        String.format("Error deleting payment, payment not found: '%s'", paymentReference));

    }

    loggingPortOut.info(this.getClass(), "[persistence.payment.delete] commited");

    return new Payment().setPaymentReference(paymentReference);

  }

}
