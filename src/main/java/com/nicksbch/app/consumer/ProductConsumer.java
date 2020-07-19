package com.nicksbch.app.consumer;

import com.nicksbch.app.Config;
import com.nicksbch.app.avro.Product;

public class ProductConsumer {
    public static void main(String[] args) {
        Consumers<Product> customerConsumers = new Consumers<>();
        customerConsumers.consume(Config.BOOTSTRAP_SERVER, Config.SCHEMA_REGISTRY, Config.PRODUCT_TOPIC);
    }
}

