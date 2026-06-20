Feature: operative delete-payment

  Background:
    * url ${functional-tests.server}
    * header Content-Type = 'application/json'
    * def setup = callonce read('helpers/create-payment.feature')
    * def paymentReference = setup.paymentReference

  Scenario: Case 01: Success - Should return 204 when deleting a "payment"
    Given path '/payments', paymentReference
    When method DELETE
    Then status 204

  Scenario: Case 02: Failure - Should return 404 if the "payment" does not exist
    Given path '/payments/17a5d895-2f44-4870-9478-14900cfe75f9'
    When method DELETE
    Then status 404

  Scenario: Case 03: Failure - Should return 400 if the "payment reference" is not a UUID
    Given path '/payments/dummy'
    When method DELETE
    Then status 400
