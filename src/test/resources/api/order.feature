# language: en
@api-login
Feature: Order

  Scenario: Order list
    Given Exists data "订单":
      | code  | productName | total | status        |
      | SN001 | laptop      | 19999 | toBeDelivered |
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

  Scenario: Order detail - no logistics
    Given Exists data "未发货的 订单":
      | code  | productName | total | recipientName | recipientMobile | recipientAddress |
      | SN001 | laptop      | 19999 | Jerry         | 415-555-2671    | New York         |
    When API query order detail with "SN001"
    Then the response order should be:
    """
      {
        "code": "SN001",
        "productName": "laptop",
        "total": 19999,
        "recipientName": "Jerry",
        "recipientMobile": "415-555-2671",
        "recipientAddress": "New York",
        "status": "toBeDelivered"
      }
    """

  Scenario: Deliver order
    Given Exists data "未发货的 订单":
      | code  |
      | SN001 |
    When API deliver order "SN001" with delivery number "SF001"
    Then order "SN001" status should be delivering and delivery number should be "SF001"

  Scenario: Order detail - with logistics
    Given Exists data "订单":
      | code  | productName | total | recipientName | recipientMobile | recipientAddress | status     | deliverNo     | deliveredAt          |
      | SN001 | laptop      | 19999 | Jerry         | 415-555-2671    | New York         | delivering | 4313751158896 | 2022-02-26T16:25:01Z |
    And exists delivery information of "4313751158896" as below:
    """
      {
          "status": 0,
          "msg": "ok",
          "result": {
              "number": "4313751158896",
              "type": "yunda",
              "typename": "Yunda Express",
              "logo": "https://api.jisuapi.com/express/static/images/logo/80/yunda.png",
              "list": [
                  {
                      "time": "2021-04-16 23:51:55",
                      "status": "Leaving Shandong Weifang Distribution Center; Going to Chengdu East District"
                  },
                  {
                      "time": "2021-04-16 23:45:47",
                      "status": "Arrived Shandong Weifang Distribution Center"
                  },
                  {
                      "time": "2021-04-16 16:47:35",
                      "status": "Shandong Qingzhou City Company-Zhao Liangtao (13606367012) has been received"
                  }
              ],
              "deliverystatus": 1,
              "issign": 0
          }
      }
      """
    When API query order detail with "SN001"
    Then the response order should be:
    """
      {
        "code": "SN001",
        "productName": "laptop",
        "total": 19999,
        "recipientName": "Jerry",
        "recipientMobile": "415-555-2671",
        "recipientAddress": "New York",
        "status": "delivering",
        "deliveredAt": "2022-02-26 16:25:01",
        "logistics": {
            "deliverNo": "4313751158896",
            "companyCode": "yunda",
            "companyName": "Yunda Express",
            "companyLogo": "https://api.jisuapi.com/express/static/images/logo/80/yunda.png",
            "details": [
                {
                    "time": "2021-04-16 23:51:55",
                    "status": "Leaving Shandong Weifang Distribution Center; Going to Chengdu East District"
                },
                {
                    "time": "2021-04-16 23:45:47",
                    "status": "Arrived Shandong Weifang Distribution Center"
                },
                {
                    "time": "2021-04-16 16:47:35",
                    "status": "Shandong Qingzhou City Company-Zhao Liangtao (13606367012) has been received"
                }
            ],
            "deliveryStatus": "On The Way",
            "isSigned": "Unreceipted"
        }
      }
    """
