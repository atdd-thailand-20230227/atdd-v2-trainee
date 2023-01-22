package com.odde.atddv2;

import com.odde.atddv2.entity.User;
import com.odde.atddv2.page.HomePage;
import com.odde.atddv2.repo.UserRepo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginSteps {
    @Autowired
    private HomePage homePage;

    @Autowired
    private Browser browser;

    @Autowired
    private UserRepo userRepo;

    @Given("exists a user with username {string} and password {string}")
    public void existsAUserWithUsernameAndPassword(String userName, String password) {
        userRepo.save(new User().setUserName(userName).setPassword(password));
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithUsernameAndPassword(String userName, String password) {
        homePage.open();
        homePage.login(userName, password);
    }

    @Then("{string} should be logged in")
    public void shouldBeLoggedIn(String userName) {
        browser.shouldHaveText("Welcome " + userName);
    }

    @Then("login failed error message should be {string}")
    public void loginFailedErrorMessageShouldBe(String message) {
        browser.shouldHaveText(message);
    }
}
