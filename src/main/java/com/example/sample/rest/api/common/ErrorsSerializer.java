package com.example.sample.rest.api.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent // Error라는 객체를 Serialize 할 때 해당 Component를 사용
@Slf4j
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // Errors안의 Error가 여러개이므로 Array에 담아 주기 위함.
        jsonGenerator.writeStartArray();

        // set field errors
        errors.getFieldErrors().stream().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("field", e.getField());
                Object rejectedValue = e.getRejectedValue();
                if(rejectedValue != null) {
                    jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
                }
                jsonGenerator.writeEndObject();
            } catch (IOException ex) {
                log.error("json serialize set field errors error!");
                ex.printStackTrace();
            }
        });

        // set global errors
        errors.getGlobalErrors().stream().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeEndObject();
            } catch (IOException ex) {
                log.error("json serialize set global errors error!");
                ex.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }
}
