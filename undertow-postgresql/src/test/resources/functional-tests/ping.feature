Feature: ping

  Background:
    * url ${functional-tests.server}
    * header Content-Type = '*/*'

  Scenario: Case 01: Success - Should return 200 for "ping"
    Given path '/ping'
    When method GET
    Then status 200
