package com.waltwang.maivc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class json {
    static String appendNewMessage(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // parse the JSON string into an ObjectNode
        ObjectNode jsonNode = mapper.readValue(jsonString, ObjectNode.class);

        // create a new item as an ObjectNode
        ObjectNode newItem = mapper.createObjectNode();
        newItem.put("role", "user");
        newItem.put("content", "What teams played in the World Series in 2021?");

        // append the new item to the JSON array
        jsonNode.withArray("conversation").add(newItem);

        // convert the updated ObjectNode back to a JSON string
        String updatedJsonString = mapper.writeValueAsString(jsonNode);
        return updatedJsonString;
    }
}
