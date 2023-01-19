package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.atddv2.entity.User;
import com.odde.atddv2.repo.UserRepo;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import okhttp3.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.openqa.selenium.By.xpath;

public class TestSteps {
    @Autowired
    UserRepo userRepo;
    private WebDriver webDriver = null;
    private Response response;

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    @After
    public void closeBrowser() {
        getWebDriver().quit();
    }

    @Given("exists a user with username {string} and password {string}")
    public void existsAUserWithUsernameAndPassword(String userName, String password) {
        userRepo.deleteAll();
        userRepo.save(new User().setUserName(userName).setPassword(password));
    }

    @SneakyThrows
    @When("api login with username {string} and password {string}")
    public void apiLoginWithUsernameAndPassword(String userName, String password) {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(new User().setUserName(userName).setPassword(password)));
        Request request = new Request.Builder().url("http://localhost:10081/users/login").post(requestBody).build();
        response = okHttpClient.newCall(request).execute();
    }

    @Then("print token")
    public void printToken() {
        System.out.println("response.header(\"token\") = " + response.header("token"));
    }

    @When("search {string} with baidu")
    public void searchWithBaidu(String keyword) {
        getWebDriver().get("http://www.baidu.com");
        webDriver.findElement(By.xpath("//*[@id='kw']")).sendKeys(keyword);
        webDriver.findElement(By.xpath("//*[@id='su']")).click();
    }

    @SneakyThrows
    @Then("print search result count")
    public void printSearchResultCount() {
        TimeUnit.SECONDS.sleep(2);
        String text = webDriver.findElement(xpath("//*[@id='container']/div[2]/div/div[2]/span")).getText();
        System.out.println("text = " + text);
    }

    @Then("test environment should be ok")
    public void testEnvironmentShouldBeOk() {
        getWebDriver().get("http://host.docker.internal:10081/");
        assertThat(getWebDriver().findElements(xpath("//*[text()='Login']"))).isNotEmpty();
        getWebDriver().quit();
    }

    @After
    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    public WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithUsernameAndPassword(String userName, String password) {
        getWebDriver().get("http://host.docker.internal:10081");
        await().ignoreExceptions().until(() -> getWebDriver().findElement(xpath("//*[@id=\"app\"]/div/form/div[2]/div/div/input")), Objects::nonNull).sendKeys(userName);
        await().ignoreExceptions().until(() -> getWebDriver().findElement(xpath("//*[@id=\"app\"]/div/form/div[3]/div/div/input")), Objects::nonNull).sendKeys(password);
        await().ignoreExceptions().until(() -> getWebDriver().findElement(xpath("//*[@id=\"app\"]/div/form/button/span")), Objects::nonNull).click();
    }

    @Then("{string} should be logged in")
    public void shouldBeLoggedIn(String userName) {
        await().ignoreExceptions().untilAsserted(() -> assertThat(getWebDriver().findElements(xpath("//*[text()='" + ("Welcome " + userName) + "']"))).isNotEmpty());
    }

    @Then("login failed error message should be {string}")
    public void loginFailedErrorMessageShouldBe(String message) {
        await().ignoreExceptions().untilAsserted(() -> assertThat(getWebDriver().findElements(xpath("//*[text()='" + message + "']"))).isNotEmpty());
    }
}
