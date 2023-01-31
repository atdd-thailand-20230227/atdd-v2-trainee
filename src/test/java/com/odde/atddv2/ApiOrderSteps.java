package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.odde.atddv2.entity.Order;
import com.odde.atddv2.repo.OrderRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiOrderSteps {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private Api api;

    @Given("exists the following orders:")
    public void existsTheFollowingOrders(DataTable table) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
        table.asMaps().forEach(map -> orderRepo.save(objectMapper.convertValue(map, Order.class)));
    }

    @When("API query order list")
    public void apiQueryOrderList() {
        api.get("orders");
    }

    @Then("the response order should be:")
    public void theResponseOrderShouldBe(String json) {
        api.responseShouldMatchJson(json);
    }
}
