package com.nicksbch.app;

import com.nicksbch.app.avro.Customer;
import com.nicksbch.app.avro.EnrichedOrder;
import com.nicksbch.app.avro.Order;
import com.nicksbch.app.avro.Product;
import com.nicksbch.app.common.SerdeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Properties;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {

        Properties applicationProperties = initProperties();
        StreamsBuilder streamsBuilder = createStream();

        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), applicationProperties);
        streams.cleanUp();
        streams.start();
    }

    private static Properties initProperties() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString());
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "order-client-app");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Config.BOOTSTRAP_SERVER);
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, Config.STATE_STORE);
        streamsConfiguration.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return streamsConfiguration;
    }

    private static StreamsBuilder createStream() {

        SpecificAvroSerde<Customer> customerSpecificAvroSerde = SerdeConfig.createSerde(Config.SCHEMA_REGISTRY);
        SpecificAvroSerde<Product> productSpecificAvroSerde = SerdeConfig.createSerde(Config.SCHEMA_REGISTRY);
        SpecificAvroSerde<Order> orderSpecificAvroSerde = SerdeConfig.createSerde(Config.SCHEMA_REGISTRY);
        SpecificAvroSerde<EnrichedOrder> enrichedOrderSpecificAvroSerde = SerdeConfig.createSerde(Config.SCHEMA_REGISTRY);

        final StreamsBuilder builder = new StreamsBuilder();

        final GlobalKTable<Long, Customer> customers = builder
                .globalTable(Config.CUSTOMER_TOPIC, Materialized.<Long, Customer, KeyValueStore<Bytes, byte[]>>as("customer-store")
                        .withKeySerde(Serdes.Long())
                        .withValueSerde(customerSpecificAvroSerde));

        final GlobalKTable<Long, Product> products = builder
                .globalTable(Config.PRODUCT_TOPIC, Materialized.<Long, Product, KeyValueStore<Bytes, byte[]>>as("product-store")
                        .withKeySerde(Serdes.Long())
                        .withValueSerde(productSpecificAvroSerde));

        final KStream<Long, Order> ordersStream = builder
                .stream(Config.ORDER_TOPIC, Consumed.with(Serdes.Long(), orderSpecificAvroSerde));

        final KStream<Long, CustomerOrder> customerOrderKStream = ordersStream.leftJoin(customers,
                        (orderId, order) -> order.getCustomerId(),
                        (order, customer) -> new CustomerOrder(customer, order));

        final KStream<Long, EnrichedOrder> output = customerOrderKStream.leftJoin(products,
                (orderId, customerOrder) -> customerOrder.getOrder().getProductId(),
                (customerOrder, product) -> new EnrichedOrder(
                        customerOrder.getOrder().getId(),
                        customerOrder.getOrder().getProductId(),
                        product.getName(),
                        product.getPrice(),
                        customerOrder.getCustomer().getId(),
                        customerOrder.getCustomer().getName(),
                        customerOrder.getCustomer().getSince(),
                        customerOrder.getOrder().getQuantity(),
                        customerOrder.getOrder().getQuantity() * product.getPrice()));

        output.to(Config.ENRICHED_ORDER_TOPIC, Produced.with(Serdes.Long(), enrichedOrderSpecificAvroSerde));

        final KStream<Long, EnrichedOrder> enrichedOrderKStream = builder.stream(Config.ENRICHED_ORDER_TOPIC, Consumed.with(Serdes.Long(), enrichedOrderSpecificAvroSerde));


        final KTable<Long, Double> stats = enrichedOrderKStream
                .map( (k, v) -> new KeyValue<>(v.getProductId(), v.getTotalAmount()))
                .groupByKey(Grouped.with(Serdes.Long(), Serdes.Double()))
                .reduce(Double::sum);

        stats.toStream().print(Printed.toSysOut());

        return builder;
    }

    private static class CustomerOrder {
        private final Customer customer;
        private final Order order;

        CustomerOrder(final Customer customer, final Order order) {
            this.customer = customer;
            this.order = order;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Order getOrder() {
            return order;
        }
    }
}