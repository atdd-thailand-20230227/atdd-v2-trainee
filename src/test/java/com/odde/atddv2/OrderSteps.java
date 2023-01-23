package com.odde.atddv2;

import com.odde.atddv2.page.OrderPage;
import com.odde.atddv2.page.WelcomePage;
import com.odde.atddv2.repo.OrderRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.zh_cn.假如;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderSteps {

    @Autowired
    private WelcomePage welcomePage;

    @Autowired
    private Browser browser;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderPage orderPage;

    @假如("存在如下订单:")
    public void 存在如下订单(DataTable table) {
    }

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
