package adrien.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.nebhale.jsonpath.JsonPath;

public class JsonUtil {

    public static JsonNode parseText(String text) throws JsonParseException, UnsupportedEncodingException, IOException {
        return parseInputStream(new ByteArrayInputStream(text.getBytes("UTF-8")));
    }

    public static JsonNode parseInputStream(InputStream inputStream) throws JsonParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(inputStream);
        JsonNode node = mapper.readTree(parser);
        return node;
    }

    public static <T> T applyPath(String expression, JsonNode jsonNode, Class<T> expectedClass) {
        JsonPath jsonPath = JsonPath.compile(expression);
        return applyPath(jsonPath, jsonNode, expectedClass);
    }

    public static <T> T applyPath(JsonPath jsonPath, JsonNode jsonNode, Class<T> expectedClass) {
        return jsonPath.read(jsonNode, expectedClass);
    }

    public static boolean validateSchema(JsonSchema jsonSchema, JsonNode jsonNode) throws ProcessingException {
        return jsonSchema.validInstance(jsonNode);
    }

    public static boolean validateSchema(JsonNode jsonSchemaAsNode, JsonNode jsonNode) throws ProcessingException {
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.byDefault();
        JsonSchema jsonSchema = jsonSchemaFactory.getJsonSchema(jsonSchemaAsNode);
        return validateSchema(jsonSchema, jsonNode);
    }

}
