package com.odde.atddv2;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class TestSteps {
    private WebDriver webDriver = null;

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    @After
    public void closeBrowser() {
        getWebDriver().quit();
    }

    @Given("exists a user with username {string} and password {string}")
    public void existsAUserWithUsernameAndPassword(String arg0, String arg1) {
    }

    @When("api login with username {string} and password {string}")
    public void apiLoginWithUsernameAndPassword(String arg0, String arg1) {
    }

    @Then("print token")
    public void printToken() {
    }

    @When("search {string} with baidu")
    public void searchWithBaidu(String arg0) {
    }

    @Then("print search result count")
    public void printSearchResultCount() {
    }

    @Then("test environment should be ok")
    public void testEnvironmentShouldBeOk() {
        getWebDriver().get("http://host.docker.internal:10081/");
        assertThat(getWebDriver().findElements(xpath("//*[text()='Login']"))).isNotEmpty();
        getWebDriver().quit();
    }

    private WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }
}
