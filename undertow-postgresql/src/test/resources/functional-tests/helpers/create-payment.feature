@ignore
Feature: Helper to create a payment

  Background:
    * url ${functional-tests.server}
    * header Content-Type = 'application/json'

  Scenario: Creates a 'payment' and extracts the identifier
    Given path '/payments'
    And request
      """
      {
        "a1": "payer-773899273774",
        "a2": "payee-998877665544",
        "a3": 100.00,
        "a4": "Test payment",
        "a5": "2030-12-31T10:00:00"
      }
      """
    When method POST
    Then status 201
    * def paymentReference = responseHeaders['a0'][0]
