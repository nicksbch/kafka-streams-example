package com.nicksbch.app.producer;

import com.nicksbch.app.common.SerdeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;

import java.util.List;
import java.util.Properties;

public class Producers<T extends SpecificRecord> {
    public void produce(final String bootstrapServers, final String schemaRegistryUrl, String topic, List<T> data) {
        final SpecificAvroSerde<T> serde = SerdeConfig.createSerde(schemaRegistryUrl);

        final Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        final KafkaProducer<Long, T> producer = new KafkaProducer<>(producerProperties, Serdes.Long().serializer(), serde.serializer());
        for(T record: data) {
            producer.send(new ProducerRecord<>(topic, (long) record.get(0), record));
        }
        producer.close();
    }
}
