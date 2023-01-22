# language: en
Feature: Get familiar with environment
  Homework 1: Familiar with the use of Cucumber, JPA, OkHttp libraries.
  `Given exists a user with username "joseph" and password "123"` use Spring Boot Data JPA to write the username and password data into the users table of the database
  `When api login with username "joseph" and password "123"` use OkHttp to send a POST request to http://localhost:10081/users/login with body { "userName": "joseph", "password": "123" }
  `Then print token` print the "Token" item of the response header of the previous POST request to the log
  ---
  Homework 2: Get familiar with Selenium
  `When search "cucumber" with baidu` use Selenium to open the browser and search "cucumber" with baidu (WebDriver has been defined in TestSteps.java)
  `Then print search result count` use Selenium to get the search result count from the search result page and print it to the log

  Scenario: Homework 1 - Print login token
    Given exists a user with username "joseph" and password "123"
    When api login with username "joseph" and password "123"
    Then print token

  Scenario: Homework 2 - Operate browser
    When search "cucumber" with baidu
    Then print search result count

  Scenario: Verify test environment
    Then test environment should be ok
