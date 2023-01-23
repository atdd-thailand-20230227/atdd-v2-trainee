package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.atddv2.entity.User;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import okhttp3.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class TestSteps {
    @Autowired
    Browser browser;
    private Response response;

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
        getWebDriver().findElement(By.xpath("//*[@id='kw']")).sendKeys(keyword);
        getWebDriver().findElement(By.xpath("//*[@id='su']")).click();
    }

    @SneakyThrows
    @Then("print search result count")
    public void printSearchResultCount() {
        TimeUnit.SECONDS.sleep(2);
        String text = getWebDriver().findElement(xpath("//*[@id='container']/div[2]/div/div[2]/span")).getText();
        System.out.println("text = " + text);
    }

    @Then("test environment should be ok")
    public void testEnvironmentShouldBeOk() {
        getWebDriver().get("http://host.docker.internal:10081/");
        assertThat(getWebDriver().findElements(xpath("//*[text()='Login']"))).isNotEmpty();
    }

    private WebDriver getWebDriver() {
        return browser.getWebDriver();
    }

}
