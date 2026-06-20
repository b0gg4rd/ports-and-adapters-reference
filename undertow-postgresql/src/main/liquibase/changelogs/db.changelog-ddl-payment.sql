--liquibase formatted sql

--changeset fcruz@bancobase.com:create-table-payment context:ddl
CREATE TABLE payment (
  id UUID NOT NULL,
  payer_id VARCHAR(200) NOT NULL,
  payee_id VARCHAR(200) NOT NULL,
  amount NUMERIC(19,4) NOT NULL,
  subject VARCHAR(500),
  execution_date TIMESTAMP NOT NULL,
  status VARCHAR(80) NOT NULL,
  created_at TIMESTAMP NOT NULL
)
