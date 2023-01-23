package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.atddv2.entity.Order;
import com.odde.atddv2.entity.User;
import com.odde.atddv2.repo.OrderRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiOrderSteps {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private String response;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private LoginSteps loginSteps;

    @Given("exists the following orders:")
    public void existsTheFollowingOrders(DataTable table) {
        ObjectMapper objectMapper = new ObjectMapper();
        table.asMaps().forEach(map -> orderRepo.save(objectMapper.convertValue(map, Order.class)));
    }

    @SneakyThrows
    @When("API query order list")
    public void apiQueryOrderList() {
        loginSteps.existsAUserWithUsernameAndPassword("j", "j");
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(new User().setUserName("j").setPassword("j")));
        Request request = new Request.Builder().url("http://localhost:10081/users/login").post(requestBody).build();
        String token = okHttpClient.newCall(request).execute().header("token");

        request = new Request.Builder()
                .url("http://localhost:10081/api/orders")
                .header("Accept", "application/json")
                .header("token", token)
                .get().build();

        response = okHttpClient.newCall(request).execute().body().string();
    }

    @SneakyThrows
    @Then("the response order should be:")
    public void theResponseOrderShouldBe(String json) {
        JSONAssert.assertEquals(json, response, false);
    }
}
