package com.nicksbch.app.producer;

import com.nicksbch.app.Config;
import com.nicksbch.app.avro.Customer;
import com.nicksbch.app.avro.Product;

public class KTableGeneratorMain {

    public static void main(String[] args) {
        Producers<Customer> customerProducers = new Producers<>();
        Producers<Product> productProducers = new Producers<>();

        customerProducers.produce(Config.BOOTSTRAP_SERVER, Config.SCHEMA_REGISTRY, Config.CUSTOMER_TOPIC, Data.customers);
        productProducers.produce(Config.BOOTSTRAP_SERVER, Config.SCHEMA_REGISTRY, Config.PRODUCT_TOPIC, Data.products);
    }
}
