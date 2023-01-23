# language: en
Feature: Order

  Scenario: Order list
    Given exists the following orders:
      | code  | productName | total | recipientName | status        |
      | SN001 | laptop      | 19999 | Jerry         | toBeDelivered |
    When API query order list
    Then the response order should be:
    """
      [{
        "code": "SN001",
        "productName": "laptop",
        "total": 19999,
        "status": "toBeDelivered"
      }]
    """
