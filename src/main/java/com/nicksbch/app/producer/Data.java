package com.nicksbch.app.producer;

import com.nicksbch.app.avro.Customer;
import com.nicksbch.app.avro.Order;
import com.nicksbch.app.avro.Product;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {
    public static List<Customer> customers = new ArrayList<>(Arrays.asList(
            new Customer(1L, "nick", Instant.now().toEpochMilli()),
            new Customer(2L, "john", Instant.now().toEpochMilli()),
            new Customer(3L, "jane", Instant.now().toEpochMilli())
            ));

    public static List<Product> products = new ArrayList<>(Arrays.asList(
            new Product(1000L, "bread", 2.5),
            new Product(2000L, "milk", 10.2),
            new Product(3000L, "butter", 53.25)
            ));

    public static List<Order> orders = new ArrayList<>(Arrays.asList(
            new Order(1L, 1000L, 1L, 2),
            new Order(2L, 2000L, 1L, 1),
            new Order(3L, 3000L, 1L, 1),
            new Order(4L, 3000L, 2L, 10),
            new Order(5L, 3000L, 1L, 5),
            new Order(6L, 1000L, 3L, 2),
            new Order(7L, 1000L, 3L, 2),
            new Order(8L, 1000L, 1L, 2),
            new Order(9L, 1000L, 2L, 17),
            new Order(10L, 2000L, 2L, 1)));
}
