package com.vo.kafka.commons.csv;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * Implementations of this class represents metadata of a CSV file.
 */
public interface CSVInformation {
    /**
     * @return the CSV schema
     */
    CsvSchema getSchema();

    /**
     * Indicates if the CSV file it is representing contains headers or not.
     *
     * @return true if the file contains headers, false other case.
     */
    boolean containsHeaders();

    /**
     * @return the POJO class that represents the CSV dataset.
     */
    Class getMappedClass();
}
