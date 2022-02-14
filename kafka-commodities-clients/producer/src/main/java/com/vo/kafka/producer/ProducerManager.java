package com.vo.kafka.producer;

import com.vo.kafka.commons.GlobalCommoditiesKafkaProperties;
import com.vo.kafka.producer.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Class to manage the process of production of commodity records to kafka.
 */
public class ProducerManager {

    private static final Logger logger = LoggerFactory.getLogger(ProducerManager.class);
    private final GlobalCommoditiesKafkaProperties prodProps;
    private final Producer producer;
    private final CSVParser parser;

    /**
     * Constructor.
     *
     * @param props contains the properties to create the Kafka producer
     */
    public ProducerManager(final GlobalCommoditiesKafkaProperties props) {
        this.prodProps = props;
        producer = initProducer();
        parser = initParser();
    }

    /**
     * @return the initialized {@link Producer} instance.
     */
    private Producer initProducer() {
        return new Producer(prodProps.getServer(), prodProps.getPort(), prodProps.getTopic());
    }

    /**
     * @return the initialized {@link CSVParser} instance.
     */
    private CSVParser initParser() {
        return new CSVParser(prodProps.getFile(), producer);
    }

    /**
     * Runs kafka the production task.
     */
    public void startProduction() {
        try {
            logger.info("Starting single thread production");
            parser.parseAndSend(producer);
            logger.info("Finishing single thread.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startParallelProduction(int parallelization) {
        logger.info("Starting multiple thread production. Setting {} threads.", parallelization);
        parser.parallelParseAndSend(producer, parallelization);
    }
}
