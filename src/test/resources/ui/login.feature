# language: en
Feature: Login

  Scenario: Login successfully
    Given exists a user with username "joseph" and password "123"
    When I login with username "joseph" and password "123"
    Then "joseph" should be logged in

  Scenario: Login failed
    Given exists a user with username "joseph" and password "123"
    When I login with username "joseph" and password "incorrect-password"
    Then login failed error message should be "Invalid username or password"
