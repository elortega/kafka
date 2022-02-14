package com.vo.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Global Commodities Kafka Producer. Allows sending String messages to a Kafka topic.
 */
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    /**
     * KafkaProducer enclosed by this class.
     */
    private final KafkaProducer<Integer, String> producer;
    /**
     * Kafka topic Id.
     */
    private final String topic;

    /**
     * Constructor.
     *
     * @param server Kafka server hostname.
     * @param port Kafka server port.
     * @param topic Kafka topic.
     */
    public Producer(final String server,
                    final String port,
                    final String topic) {

        //Enable idempotence flag for Kafka producer.
        boolean ENABLE_IDEMPOTENCE = true;

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server + ":" + port);
        // For now lets hardcode this property.
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "CommoditiesProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, ENABLE_IDEMPOTENCE);

        producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    /**
     * Constructor.
     *
     * @param producer underlined kafka producer to forward the messages.
     * @param topic to which send the messages.
     */
    public Producer(final KafkaProducer producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    /**
     * @return the {@link KafkaProducer} instance.
     */
    KafkaProducer<Integer, String> get() {
        return producer;
    }

    /**
     * @return the kafka topic to which this instance produces.
     */
    public String getTopic() {
        return this.topic;
    }

    /**
     * Sends a message asynchronously.
     *
     * @param message to send.
     * @return a Future promise with the RecordMetadata sent by the server.
     */
    public Future<RecordMetadata> sendAsyncMessage(String message) {
        logger.trace("Sending Async message: {}", message);
        // We send the records without key as it is a unique topic and we do not care about order.
        return this.producer.send(new ProducerRecord<>(topic, message));
    }

    /**
     * Sends a message synchronously blocking until the computation of is complete.
     *
     * @param message to send.
     * @return the RecordMetadata sent by the server.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public RecordMetadata sendSyncMessage(String message) throws ExecutionException,
            InterruptedException {
        logger.trace("Sending Sync message: {}", message);
        // We send the records without key as it is a unique topic and we do not care about order.
        return this.producer.send(new ProducerRecord<>(topic, message)).get();
    }
}



