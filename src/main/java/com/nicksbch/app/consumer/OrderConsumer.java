package com.nicksbch.app.consumer;

import com.nicksbch.app.Config;
import com.nicksbch.app.avro.Order;

public class OrderConsumer {
    public static void main(String[] args) {
        Consumers<Order> customerConsumers = new Consumers<>();
        customerConsumers.consume(Config.BOOTSTRAP_SERVER, Config.SCHEMA_REGISTRY, Config.ORDER_TOPIC);
    }
}

