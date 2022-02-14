package com.vo.kafka.commons.pojos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vo.kafka.commons.RowIndexSerializer;

/**
 * POJO representation of the CSV Global Commodity Trade Statistics.
 * <br>
 * CSV headers:
 * <ul>
 * <li>country_or_area</li>
 * <li>year</li>
 * <li>comm_code</li>
 * <li>commodity</li>
 * <li>flow</li>
 * <li>trade_usd</li>
 * <li>weight_kg</li>
 * <li>quantity_name</li>
 * <li>quantity</li>
 * <li>category</li>
 * </ul>
 *
 * <br>
 * The CSV file this POJO represents can be downloaded from
 * https://www.kaggle.com/unitednations/global-commodity-trade-statistics
 */
public class GlobalCommodity {

    private String country_or_area;
    private int year;
    private String comm_code;
    private String commodity;
    private String flow;
    private long trade_usd;
    private long weight_kg;
    private String quantity_name;
    private double quantity;
    private String category;
    @JsonSerialize(using = RowIndexSerializer.class)
    /** Extra field, custom serialized, to control parsing progress. */
    private String progress;

    public GlobalCommodity() {
        this.progress = "";
    }

    public GlobalCommodity(String country_or_area, int year, String comm_code, String commodity,
                           String flow, int trade_usd, int weight_kg, String quantity_name,
                           int quantity, String category, String row) {
        super();
        this.country_or_area = country_or_area;
        this.year = year;
        this.comm_code = comm_code;
        this.commodity = commodity;
        this.flow = flow;
        this.trade_usd = trade_usd;
        this.weight_kg = weight_kg;
        this.quantity_name = quantity_name;
        this.quantity = quantity;
        this.category = category;
        this.progress = row;
    }

    public String getCountry_or_area() {
        return country_or_area;
    }

    public void setCountry_or_area(String countryOrArea) {
        this.country_or_area = countryOrArea;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getComm_code() {
        return comm_code;
    }

    public void setComm_code(String comm_code) {
        this.comm_code = comm_code;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public long getTrade_usd() {
        return trade_usd;
    }

    public void setTrade_usd(long trade_usd) {
        this.trade_usd = trade_usd;
    }

    public long getWeight_kg() {
        return weight_kg;
    }

    public void setWeight_kg(long weight_kg) {
        this.weight_kg = weight_kg;
    }

    public String getQuantity_name() {
        return quantity_name;
    }

    public void setQuantity_name(String quantity_name) {
        this.quantity_name = quantity_name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProgress() {
        return progress;
    }

    public void setRowIndex(String rowIndex) {
        this.progress = rowIndex;
    }

    public String toString() {
        return commodity + " - " + trade_usd;
    }


}
