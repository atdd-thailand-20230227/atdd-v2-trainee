package com.odde.atddv2;

import com.odde.atddv2.page.OrderPage;
import com.odde.atddv2.page.WelcomePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderSteps {

    @Autowired
    private WelcomePage welcomePage;

    @Autowired
    private Browser browser;

    @Autowired
    private OrderPage orderPage;

    @When("create order with data below:")
    public void createOrderWithDataBelow(DataTable table) {
        welcomePage.goToOrders();
        orderPage.addOrder(table.asMaps().get(0));
    }

    @Then("the following order should be displayed:")
    public void theFollowingOrderShouldBeDisplayed(DataTable table) {
        table.asList().forEach(browser::shouldHaveText);
    }
}
