package com.vo.kafka.producer.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.vo.kafka.commons.csv.CSVInformation;
import com.vo.kafka.producer.KafkaWriter;
import com.vo.kafka.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Runnable class to produce to Kafka, as JSON representation, the rows in a CSV file.
 *
 * @param <T> class representing the POJO for the CSV contents.
 */
public class CSVProducerRunnable<T> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CSVProducerRunnable.class);
    private final String csvFile;
    private final CsvSchema schema;
    private final String runnableId;
    private final Class<T> classType;
    private final Producer producer;
    private final CountDownLatch countDownLatch;

    /**
     * Constructor.
     *
     * @param csvFile file containing the records to send.
     * @param runnableId id for the thread running this task.
     * @param producer used to send the kafka messages.
     * @param csvInfo meta-info of the CSV file.
     * @param countDownLatch synchronization latch.
     */
    public CSVProducerRunnable(String csvFile, String runnableId, Producer producer,
                               CSVInformation csvInfo, CountDownLatch countDownLatch) {
        this.csvFile = csvFile;
        this.schema = csvInfo.getSchema();
        this.runnableId = runnableId;
        this.classType = csvInfo.getMappedClass();
        this.producer = producer;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            logger.info("Starting thred {} for CSV file {}", this.runnableId, this.csvFile);
            // First we read the CSV file into an iterator
            CsvMapper csvMapper = new CsvMapper();
            MappingIterator<T> orderLines = csvMapper.readerFor(classType)
                    .with(schema)
                    .readValues(new File(csvFile));
            KafkaWriter wr = new KafkaWriter(producer, producer.getTopic());
            // Now we serialize into a JSON
            new ObjectMapper()//.registerModule(module)
                    .configure(SerializationFeature.INDENT_OUTPUT, true).writer()
                    .writeValues(wr).writeAll(orderLines.readAll());

            countDownLatch.countDown();
            logger.info("Finished thread {} for CSV file {}", this.runnableId, this.csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
