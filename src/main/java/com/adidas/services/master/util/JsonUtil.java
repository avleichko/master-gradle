package com.adidas.services.master.util;

import com.adidas.services.master.exception.CommonMasterServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtil {

    public static String objectToJson(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new CommonMasterServiceException(e);
        }
    }

    //TODO needs to test it
    public static <T> T jsonToPojo(String jsonString, Class<?> target) throws IOException, ClassNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, objectMapper .getTypeFactory().constructCollectionType(List.class, Class.forName(target.getName())));
    }
}
