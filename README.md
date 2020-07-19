# Kakfa Streams Example

## Summary
Quick example on how to build a basic Kafka Streams application based on the Confluent examples.
It has built-in Kafka producers and consumers to run experiments.

This is a playground. I run everything from Intellij and do not generate any jar files.
 
## Dependencies
- Docker
- JDK-11

## Setup
There are 4 topics:
- Input data: Order, Customer, Product
- Kafka-Streams output: EnrichedOrder

Compile the project to generate the Java classes from the Avro schemas.
```
mvn compile
```
If you are using Intellij, you need to set `target/generated-sources` as a source folder in `Project Structure`.

Start the docker containers and create the 4 topics.
```
docker-compose up

docker exec -it kafka kafka-topics --create --topic order --zookeeper zookeeper:32181 --partitions 4 --replication-factor 1
docker exec -it kafka kafka-topics --create --topic customer --zookeeper zookeeper:32181 --partitions 4 --replication-factor 1
docker exec -it kafka kafka-topics --create --topic product --zookeeper zookeeper:32181 --partitions 4 --replication-factor 1
docker exec -it kafka kafka-topics --create --topic enrichedorder --zookeeper zookeeper:32181 --partitions 4 --replication-factor 1
```

## Producers and Consumers
Under `src/main/java/com/nicksbch/app/consumer`

There are 4 consumers for all 4 topics:
- CustomerConsumer
- EnrichedOrderConsumer
- OrderConsumer
- ProductConsumer

Under `src/main/java/com/nicksbch/app/producer`  
- Run `KTableGeneratorMain` to send customer and product records to the Customer and Product Kafka topics.
- Run `OrderGeneratorMain` to send some orders to the Order Kafka topic.

## Run the app
Run the Main class to start the app.
It will create 2 KTables based on the customer and product topics.
It creates a KStream from the order topic and joins the 2 KTables to it to create an EnrichedOrder and send it back to Kafka.

The new EnrichedOrder KStream is then used to run an aggregation operation and print the output to the current terminal. 