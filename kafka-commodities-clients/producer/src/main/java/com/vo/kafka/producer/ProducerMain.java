package com.vo.kafka.producer;

import com.vo.kafka.commons.GlobalCommoditiesKafkaProperties;
import com.vo.kafka.commons.Util;

/**
 * Main class for the Global Commodity Producer component.
 */
public class ProducerMain {
    public static void main(String[] args) {
        ProducerManager pm =
                new ProducerManager(new GlobalCommoditiesKafkaProperties(Util.loadProperties()));
//        pm.startProduction();
        pm.startParallelProduction(2);
    }
}
