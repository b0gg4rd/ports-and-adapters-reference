Feature: operative retrieve-all-payments

  Background:
    * url ${functional-tests.server}
    * header Content-Type = 'application/json'
    * def setup = callonce read('helpers/create-payment.feature')

  Scenario: Case 01: Success - Should return 200 with the list of "payments"
    Given path '/payments'
    And param page = 0
    And param size = 10
    When method GET
    Then status 200
    And match response.a4 == '#array'

  Scenario: Case 02: Success - Should return 200 with default pagination
    Given path '/payments'
    When method GET
    Then status 200
    And match response.a0 == 0
    And match response.a1 == 10
