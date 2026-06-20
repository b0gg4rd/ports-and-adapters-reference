package net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis;

import net.coatli.reference.portsandadapters.infrastructure.adapter.out.persistence.postgresql.mybatis.model.PaymentRow;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MyBatisPaymentMapper {

  @Insert("""
    INSERT INTO payment
    (id, payer_id, payee_id, amount, subject, execution_date, status, created_at)
    VALUES
    (#{id}::uuid, #{payerId}, #{payeeId}, #{amount}, #{subject}, #{executionDate}, #{status}, #{createdAt})
  """)
  int insert(PaymentRow paymentRow);

  @ConstructorArgs({
    @Arg(column = "id",             name = "id",            javaType = String.class),
    @Arg(column = "payer_id",       name = "payerId",       javaType = String.class),
    @Arg(column = "payee_id",       name = "payeeId",       javaType = String.class),
    @Arg(column = "amount",         name = "amount",        javaType = BigDecimal.class),
    @Arg(column = "subject",        name = "subject",       javaType = String.class),
    @Arg(column = "execution_date", name = "executionDate", javaType = LocalDateTime.class),
    @Arg(column = "status",         name = "status",        javaType = String.class),
    @Arg(column = "created_at",     name = "createdAt",     javaType = LocalDateTime.class)
  })
  @Select("""
    SELECT id, payer_id, payee_id, amount, subject, execution_date, status, created_at
    FROM payment
    ORDER BY created_at DESC
    LIMIT #{size} OFFSET #{offset}
  """)
  List<PaymentRow> selectAll(@Param("size") int size, @Param("offset") int offset);

  @Select("SELECT COUNT(*) FROM payment")
  long count();

  @ConstructorArgs({
    @Arg(column = "id",             name = "id",            javaType = String.class),
    @Arg(column = "payer_id",       name = "payerId",       javaType = String.class),
    @Arg(column = "payee_id",       name = "payeeId",       javaType = String.class),
    @Arg(column = "amount",         name = "amount",        javaType = BigDecimal.class),
    @Arg(column = "subject",        name = "subject",       javaType = String.class),
    @Arg(column = "execution_date", name = "executionDate", javaType = LocalDateTime.class),
    @Arg(column = "status",         name = "status",        javaType = String.class),
    @Arg(column = "created_at",     name = "createdAt",     javaType = LocalDateTime.class)
  })
  @Select("""
    SELECT id, payer_id, payee_id, amount, subject, execution_date, status, created_at
    FROM payment
    WHERE id = #{id}::uuid
  """)
  PaymentRow selectById(@Param("id") String id);

  @Update("""
    UPDATE payment
    SET amount = #{amount}, subject = #{subject}, execution_date = #{executionDate}
    WHERE id = #{id}::uuid
  """)
  int update(PaymentRow paymentRow);

  @Delete("""
    DELETE FROM payment
    WHERE id = #{id}::uuid
  """)
  int delete(@Param("id") String id);

}
