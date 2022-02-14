package com.vo.kafka.commons.csv;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.vo.kafka.commons.pojos.GlobalCommodity;

/**
 * This class provides access to information regarding Global Commodity Trade CSV files.
 * <p>
 * The CSV file this POJO represents can be downloaded from {@see https://www.kaggle.com/unitednations/global-commodity-trade-statistics}
 */
public class GlobalCommodityCSV implements CSVInformation {

    final boolean hasHeaders;

    /**
     * Constructor.
     */
    public GlobalCommodityCSV(boolean headers) {
        this.hasHeaders = headers;
    }

    @Override
    public CsvSchema getSchema() {
        CsvSchema result;
        if (hasHeaders) {
            result = CsvSchema.emptySchema().withHeader();
        } else {
            result = CsvSchema.builder()
                    .addColumn("country_or_area", CsvSchema.ColumnType.STRING)
                    .addColumn("year", CsvSchema.ColumnType.NUMBER)
                    .addColumn("comm_code", CsvSchema.ColumnType.STRING)
                    .addColumn("commodity", CsvSchema.ColumnType.STRING)
                    .addColumn("flow", CsvSchema.ColumnType.STRING)
                    .addColumn("trade_usd", CsvSchema.ColumnType.NUMBER)
                    .addColumn("weight_kg", CsvSchema.ColumnType.NUMBER)
                    .addColumn("quantity_name", CsvSchema.ColumnType.STRING)
                    .addColumn("quantity", CsvSchema.ColumnType.NUMBER)
                    .addColumn("category", CsvSchema.ColumnType.STRING)
                    .build();
        }
        return result;
    }

    @Override
    public boolean containsHeaders() {
        return this.hasHeaders;
    }

    @Override
    public Class getMappedClass() {
        return GlobalCommodity.class;
    }
}

