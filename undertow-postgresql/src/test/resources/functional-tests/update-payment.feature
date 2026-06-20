Feature: operative update-payment

  Background:
    * url ${functional-tests.server}
    * header Content-Type = 'application/json'
    * def setup = callonce read('helpers/create-payment.feature')
    * def paymentReference = setup.paymentReference

  Scenario: Case 01: Success - Should return 200 when updating a "payment"
    Given path '/payments', paymentReference
    And request
      """
      {
        "a1": 200.00,
        "a2": "Updated subject",
        "a3": "2030-12-31T12:00:00"
      }
      """
    When method PUT
    Then status 200
    And match responseHeaders['a0'][0] == '#uuid'

  Scenario: Case 02: Failure - Should return 404 if the "payment" does not exist
    Given path '/payments/17a5d895-2f44-4870-9478-14900cfe75f9'
    And request
      """
      {
        "a1": 200.00
      }
      """
    When method PUT
    Then status 404

  Scenario: Case 03: Failure - Should return 400 if a1 is negative
    Given path '/payments', paymentReference
    And request
      """
      {
        "a1": -50.00
      }
      """
    When method PUT
    Then status 400
