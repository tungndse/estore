package com.coldev.estore.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneyBigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(
            BigDecimal bigDecimal,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        if (bigDecimal != null) {
            jsonGenerator.writeString(bigDecimal.stripTrailingZeros().toPlainString());
        } else {
            jsonGenerator.writeNull();
        }
    }
}
