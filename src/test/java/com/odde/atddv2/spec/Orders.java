package com.odde.atddv2.spec;

import com.github.leeonky.jfactory.Global;
import com.github.leeonky.jfactory.Spec;
import com.github.leeonky.jfactory.Trait;

import static com.odde.atddv2.entity.Order.OrderStatus.toBeDelivered;

public class Orders {

    @Global
    public static class Order extends Spec<com.odde.atddv2.entity.Order> {
        public void main() {
            property("id").ignore();
        }

        @Trait
        public void ToBeDelivered() {
            property("status").value(toBeDelivered);
            property("deliverNo").value(null);
            property("deliveredAt").value(null);
        }
    }
}
