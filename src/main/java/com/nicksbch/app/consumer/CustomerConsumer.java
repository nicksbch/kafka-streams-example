package com.nicksbch.app.consumer;

import com.nicksbch.app.Config;
import com.nicksbch.app.avro.Customer;

public class CustomerConsumer {
    public static void main(String[] args) {
        Consumers<Customer> customerConsumers = new Consumers<>();
        customerConsumers.consume(Config.BOOTSTRAP_SERVER, Config.SCHEMA_REGISTRY, Config.CUSTOMER_TOPIC);
    }
}

