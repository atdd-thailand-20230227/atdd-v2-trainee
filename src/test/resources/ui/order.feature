# language: en
@ui-login
Feature: Order

  Scenario: Create order
    When create order with data below:
      | Order number | Product name | Total | Recipient name | Recipient mobile | Recipient address | Status          |
      | SN001        | T-shirt      | 19    | Tom            | 415-555-2671     | New York          | To be delivered |
    Then the following order should be displayed:
      | SN001 | T-shirt | $19 | To be delivered |
