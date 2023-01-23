package com.odde.atddv2.page;

import com.odde.atddv2.Browser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderPage {

    @Autowired
    public Browser browser;

    public void addOrder(Map<String, String> order) {
        browser.clickByText("Add Order");
        order.forEach((placeholder, text) -> {
            if (!placeholder.equals("Status")) {
                browser.inputTextByPlaceholder(placeholder, text);
            }
        });
        browser.selectTextByPlaceholder("Status", order.get("Status"));
        browser.clickByText("Submit");
        browser.shouldNotHaveText("Submit");
    }
}
