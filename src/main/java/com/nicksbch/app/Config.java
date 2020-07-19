package com.nicksbch.app;


public class Config {
    public static final String ORDER_TOPIC = "order";
    public static final String CUSTOMER_TOPIC = "customer";
    public static final String PRODUCT_TOPIC = "product";
    public static final String ENRICHED_ORDER_TOPIC = "enrichedorder";


    public static final String BOOTSTRAP_SERVER = "localhost:9092";
    public static final String SCHEMA_REGISTRY = "http://localhost:8081";
    public static final String STATE_STORE = "/tmp/kafka-streams-state-stores";

}
