package com.nicksbch.app.consumer;

import com.nicksbch.app.Config;
import com.nicksbch.app.avro.EnrichedOrder;

public class EnrichedOrderConsumer {
    public static void main(String[] args) {
        Consumers<EnrichedOrder> enrichedOrderConsumers = new Consumers<>();
        enrichedOrderConsumers.consume(Config.BOOTSTRAP_SERVER, Config.SCHEMA_REGISTRY, Config.ENRICHED_ORDER_TOPIC);
    }
}

