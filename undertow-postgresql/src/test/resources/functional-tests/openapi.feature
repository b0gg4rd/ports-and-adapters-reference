Feature: openapi

  Background:
    * url ${functional-tests.root-server}

  Scenario: Case 01: Success - Should return 200 for "GET /openapi/payments.yml"
    Given path '/openapi/payments.yml'
    When method GET
    Then status 200
    And match responseHeaders['Content-Type'][0] contains 'application/yaml'
