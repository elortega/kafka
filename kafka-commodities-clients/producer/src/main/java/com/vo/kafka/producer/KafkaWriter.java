package com.vo.kafka.producer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Class, that extends an OutputStream writer, to write to a Kafka topic.
 */
public class KafkaWriter extends OutputStream {

    private final Producer prod;
    private final String topic;

    /**
     * Constructor
     * @param producer used to send messages to kafka server.
     * @param topic to which send the messages.
     */
    public KafkaWriter(Producer producer, String topic) {
        this.prod = producer;
        this.topic = topic;
    }

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b) throws IOException {

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        prod.sendAsyncMessage(new String(b, off, len, StandardCharsets.UTF_8));
    }
}
