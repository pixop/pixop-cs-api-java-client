package com.pixop.sdk.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ISODateToSDKDateSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(final String _isoDate,
                          final JsonGenerator _jsonGenerator,
                          final SerializerProvider _serializerProvider) throws IOException {
        final String[] parts = _isoDate.replace('T', ' ').split("\\."); // split on ms

        _jsonGenerator.writeObject(parts[0]);
    }
}
