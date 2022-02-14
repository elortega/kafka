package com.vo.kafka.producer.csv;

import com.vo.kafka.commons.Util;
import com.vo.kafka.commons.csv.GlobalCommodityCSV;
import com.vo.kafka.producer.KafkaWriter;
import com.vo.kafka.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Parser class to convert a CSV file, with headers included, into JSON serialized String of the
 * {@link T} POJO and send it using the {@link KafkaWriter} defined.
 *
 * @param <T> type class that represents the JSON representation of the CSV file being parsed.
 */
public class CSVParser<T> {

    private static final String CSV_EXTENSION = ".csv";
    private static final String TMP_FILE_PREFIX = "file_";
    private static final String THREAD_PREFIX = "Runnable_";
    private static final String SINGLE_RUNNABLE = "Single Runnable";
    private static final int MAX_PARALLELIZATION = 200;
    private static final Logger logger = LoggerFactory.getLogger(CSVParser.class);

    private final int FIRST_FILE = 0;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final String csvFile;
    private final long totalRecords;
    private final String temporaryDirectory;


    /**
     * Constructor.
     *
     * @param csvFile this parser manages.
     * @param producer
     */
    public CSVParser(String csvFile, Producer producer) {
        this.csvFile = csvFile;
        this.totalRecords = countLines();
        this.temporaryDirectory = Util.getSystemTempDirectory();
    }

    /**
     * Count the number of lines in the {@link #csvFile}.
     *
     * @return the number of records in the file or -1 if couldn't be counted.
     */
    private long countLines() {
        long result = -1;
        try (Stream<String> fileStream = Files.lines(Paths.get(this.csvFile))) {
            result = (int) fileStream.count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Single thread to parse and produce the contents.
     *
     * @param producer to which produce the contents.
     * @throws IOException
     */
    public void parseAndSend(final Producer producer) throws IOException {

        final CountDownLatch latch = new CountDownLatch(1);
        Thread t = new Thread(new CSVProducerRunnable(this.csvFile, SINGLE_RUNNABLE, producer,
                new GlobalCommodityCSV(true), latch));
        t.start();
        try {
            // Let's wait for the thread to finish
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Multi thread execution. The method creates a number of thread and splits the file to produce
     * the contents and waits for the tasks to finish before returning.
     *
     * @param producer
     * @param parallelization
     */
    public void parallelParseAndSend(final Producer producer, final int parallelization) {

        ExecutorService executorService = Executors.newFixedThreadPool(parallelization);
        final int numTasks = adjustParallelization(parallelization);
        String[] files = new String[numTasks];
        files = splitCsv(numTasks);

        final CountDownLatch latch = new CountDownLatch(parallelization);
        for (int fileIndex = 0; fileIndex < parallelization; fileIndex++) {
            // the first file will contain headers, that's why the GlobalCommodityCSV is built with that condition.
            executorService.submit(
                    new CSVProducerRunnable<T>(files[fileIndex], THREAD_PREFIX + fileIndex,
                            producer, new GlobalCommodityCSV(fileIndex == FIRST_FILE), latch));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Works out the parallelization suitable versus the requested so the process doesn't over
     * parallelize.
     *
     * @param requiredParallelization - number of tasks in which divide the computation.
     * @return the suitable parallelization value.
     */
    private int adjustParallelization(final int requiredParallelization) {
        int result = requiredParallelization;
        if (requiredParallelization > MAX_PARALLELIZATION) {
            result = MAX_PARALLELIZATION;
        }
        if (result > totalRecords) {
            result = 1;
        }
        return result;
    }

    /**
     * Splits {@link #csvFile} into a number of files, writing into each them the same number of
     * lines except the last file, which will include the remainder rows that cannot be spread
     * equally.
     *
     * @param numFiles to divide the
     * @return an Array of Strings with the file paths of the files generated.
     */
    private String[] splitCsv(int numFiles) {

        long linesPerFile = totalRecords / numFiles;
        long restLines = totalRecords % linesPerFile;
        logger.trace("File {} contains a total of {} lines: ", csvFile, totalRecords);
        logger.trace(
                "After splitting, every file will contain {} lines and last file will hav an extra {} lines" +
                        linesPerFile, restLines);

        String[] fileNames = new String[numFiles];
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.csvFile));
            for (int fileIndex = 0; fileIndex < numFiles; fileIndex++) {
                fileNames[fileIndex] = temporaryDirectory + TMP_FILE_PREFIX +
                        fileIndex + CSV_EXTENSION;
                BufferedWriter out = new BufferedWriter(new FileWriter(fileNames[fileIndex]));
                saveLinesToBuffer(linesPerFile, br, out);
                //Last file will contain also the spare lines.
                if (fileIndex == numFiles - 1) {
                    saveLinesToBuffer(restLines, br, out);
                }
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNames;
    }

    /**
     * Moves the buffer and saves a number of lines form the reader buffer to the writer buffer.
     *
     * @param numLines to save.
     * @param br reader.
     * @param out writer.
     * @throws IOException -.
     */
    private void saveLinesToBuffer(long numLines, BufferedReader br, BufferedWriter out) throws
            IOException {
        for (int j = 0; j < numLines; j++) {
            String strLine = br.readLine();
            if (strLine != null) {
                out.write(strLine);
                out.newLine();
            }
        }
    }
}
