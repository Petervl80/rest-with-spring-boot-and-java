package com.github.petervl80.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GenderSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String gender, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        String formalizedGender = "Male".equals(gender) ? "M" : "F";
        generator.writeString(formalizedGender);
    }
}
