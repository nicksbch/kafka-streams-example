package com.nicksbch.app.producer;

import com.nicksbch.app.Config;
import com.nicksbch.app.avro.Order;

public class OrderGeneratorMain {

    public static void main(String[] args) {
        Producers<Order> orderProducers = new Producers<>();
        orderProducers.produce(Config.BOOTSTRAP_SERVER, Config.SCHEMA_REGISTRY, Config.ORDER_TOPIC, Data.orders);
    }
}
