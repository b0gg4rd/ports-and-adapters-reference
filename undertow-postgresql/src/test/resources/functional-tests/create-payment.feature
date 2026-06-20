Feature: operative create-payment

  Background:
    * url ${functional-tests.server}
    * header Content-Type = 'application/json'

  Scenario: Case 01: Success - Should return 201 when creating a "payment"
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
    And match responseHeaders['a0'][0] == '#uuid'

  Scenario: Case 02: Failure - Should return 400 when attribute a1 is missing
    Given path '/payments'
    And request
      """
      {
        "a2": "payee-998877665544",
        "a3": 100.00
      }
      """
    When method POST
    Then status 400

  Scenario: Case 03: Failure - Should return 400 when attribute a2 is missing
    Given path '/payments'
    And request
      """
      {
        "a1": "payer-773899273774",
        "a3": 100.00
      }
      """
    When method POST
    Then status 400

  Scenario: Case 04: Failure - Should return 400 when a1 is empty
    Given path '/payments'
    And request
      """
      {
        "a1": "",
        "a2": "payee-998877665544",
        "a3": 100.00
      }
      """
    When method POST
    Then status 400

  Scenario: Case 05: Failure - Should return 400 when a3 is negative
    Given path '/payments'
    And request
      """
      {
        "a1": "payer-773899273774",
        "a2": "payee-998877665544",
        "a3": -1.00
      }
      """
    When method POST
    Then status 400

  Scenario: Case 06: Failure - Should return 400 when a5 is a past date
    Given path '/payments'
    And request
      """
      {
        "a1": "payer-773899273774",
        "a2": "payee-998877665544",
        "a3": 100.00,
        "a5": "2000-01-01T00:00:00"
      }
      """
    When method POST
    Then status 400
