package com.vo.kafka.commons;

import java.util.Properties;

/**
 * This class contains the properties for the Global Commodities Producer.
 */
public class GlobalCommoditiesKafkaProperties {

    /**
     * Property key for the CSV file reference
     */
    public final String CSV_FILE_PROP = "csv.file";
    /**
     * Property key for the temporary directory
     */
    public final String TMP_DIR_PROP = "temp.directory";
    /**
     * Property key for the Kafka server hostname
     */
    public final String KAFKA_SERVER_PROP = "kafka.server";
    /**
     * Property key for the Kafka server port
     */
    public final String KAFKA_PORT_PROP = "kafka.port";
    /**
     * Property key for the Kafka topic
     */
    public final String KAFKA_TOPIC_PROP = "kafka.topic";

    private final String server;
    private final String port;
    private final String topic;
    private final String file;
    private final String tmpDir;

    /**
     * Constructor.
     *
     * @param props used.
     */
    public GlobalCommoditiesKafkaProperties(Properties props) {
        this.server = props.getProperty(KAFKA_SERVER_PROP);
        this.tmpDir = props.getOrDefault(TMP_DIR_PROP,"/tmp").toString();
        this.port = props.getProperty(KAFKA_PORT_PROP);
        this.topic = props.getProperty(KAFKA_TOPIC_PROP);
        this.file = props.getProperty(CSV_FILE_PROP);
    }

    public String getServer() {
        return server;
    }

    public String getPort() {
        return port;
    }

    public String getTopic() {
        return topic;
    }

    public String getFile() {
        return file;
    }
}
