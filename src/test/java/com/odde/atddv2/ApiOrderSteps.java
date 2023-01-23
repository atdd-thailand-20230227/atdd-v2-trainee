package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.odde.atddv2.entity.Order;
import com.odde.atddv2.repo.OrderRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

import static com.odde.atddv2.entity.Order.OrderStatus.delivering;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiOrderSteps {

    @Autowired
    MockServer mockServer;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private Api api;
    @Value("${binstd-endpoint.key}")
    private String binstdAppKey;

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

    @When("API query order detail with {string}")
    public void apiQueryOrderDetailWith(String code) {
        api.get(String.format("orders/%s", code));
    }

    @When("API deliver order {string} with delivery number {string}")
    public void apiDeliverOrderWithDeliveryNumber(String order, String deliverNo) {
        api.post(String.format("orders/%s/deliver", order), new HashMap<String, String>() {{
            put("deliverNo", deliverNo);
        }});
    }

    @Then("order {string} status should be delivering and delivery number should be {string}")
    public void orderStatusShouldBeAndDeliveryNumberShouldBe(String order, String deliverNo) {
        assertThat(orderRepo.findByCode(order))
                .hasFieldOrPropertyWithValue("deliverNo", deliverNo)
                .hasFieldOrPropertyWithValue("status", delivering);
    }

    @And("exists delivery information of {string} as below:")
    public void existsDeliveryInformationOfAsBelow(String deliverNo, String json) {
        mockServer.getJson("/express/query", (request) -> request.withQueryStringParameter("appkey", binstdAppKey)
                .withQueryStringParameter("type", "auto")
                .withQueryStringParameter("number", deliverNo), json);
    }
}
