package com.vo.kafka.producer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * JSON Serializer to append the record row index field to GlobalCommodity.
 */
public class RowIndexSerializer extends StdSerializer<String> {

    private static final long serialVersionUID = 1L;
    public RowIndexSerializer() {
        this(null);
    }

    public RowIndexSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(
            String value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        long index = 10;
//            jgen.writeStartObject();
//            jgen.writeStartObject();
//            value.setRowIndex(index + "/" + totalRecords);
//            jgen.writeString(value);
        jgen.writeString("ayyyy");
//            jgen.writeObjectFieldStart("year");
//            jgen.writeNumberField("year", 4000);
//            jgen.writeEndObject();
    }
}