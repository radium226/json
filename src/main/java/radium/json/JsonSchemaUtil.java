package radium.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.common.base.Supplier;

public class JsonSchemaUtil {

    public static JsonSchema parseText(String text) throws JsonParseException, UnsupportedEncodingException, IOException, ProcessingException {
        JsonNode schemaAsNode = JsonUtil.parseText(text);
        return parseNode(schemaAsNode);
    }

    public static JsonSchema parseNode(JsonNode node) throws ProcessingException {
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();
        JsonSchema schema = schemaFactory.getJsonSchema(node);
        return schema;
    }

    public static boolean validateNode(JsonSchema schema, JsonNode node) throws ProcessingException {
        return schema.validInstance(node);
    }

    public static boolean validateNode(Supplier<JsonSchema> schemaSupplier, JsonNode node) throws ProcessingException {
        return validateNode(schemaSupplier.get(), node);
    }

}
