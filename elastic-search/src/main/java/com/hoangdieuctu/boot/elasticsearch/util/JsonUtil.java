package com.hoangdieuctu.boot.elasticsearch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class JsonUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private ObjectMapper objectMapper;

    public JsonUtil() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    public <T> Optional<T> toObject(String data, Class<T> clazz) {
        if (data == null || data.isEmpty() || Objects.isNull(clazz)) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(data, clazz));
        } catch (JsonProcessingException e) {
            logger.error("Error while parsing json. ", e);
            return Optional.empty();
        }
    }

    public Optional<String> toJson(Object data) {
        if (Objects.isNull(data)) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            logger.error("Error while parsing json. ", e);
            return Optional.empty();
        }
    }
}