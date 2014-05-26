package radium.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.nebhale.jsonpath.JsonPath;

public class Json {

    public static class Validate {

        private JsonNode jsonNode;

        protected Validate(JsonNode jsonNode) {
            super();

            this.jsonNode = jsonNode;
        }

        public boolean against(JsonSchema jsonSchema) throws ProcessingException {
            return jsonSchema.validInstance(jsonNode);
        }

    }

    public static class Select {

    	public static class On {
    		
    		public JsonNode node;
    		
    		public On(JsonNode node) {
    			super();
    			
    			this.node = node;
    		}
    		
    		
    		public List<JsonNode> asNodeList() {
    			return asNodeList(Predicates.<JsonNode>alwaysTrue());
    		}
    		
    		public List<JsonNode> asNodeList(Predicate<JsonNode> predicate) {
    			return Lists.newArrayList(Iterables.filter(node, predicate));
    		}
    		
    		public JsonNode asNode() {
    			return node;
    		}
    		
    	}
    	
        private JsonPath path;

        protected Select(JsonPath path) {
            super();

            this.path = path;
        }

        public On on(JsonNode node) {
            return new On(path.read(node, JsonNode.class));
        }

    }

    public static class WhichSchemaSupplier<T extends Supplier<JsonSchema>> {

        private Set<T> schemaSuppliers;

        protected WhichSchemaSupplier(Set<T> schemaSuppliers) {
            super();

            this.schemaSuppliers = schemaSuppliers;
        }

        public T validate(JsonNode node) throws ProcessingException {
            T validSchemaSupplier = null;
            for (T schemaSupplier : schemaSuppliers) {
                if (JsonSchemaUtil.validateNode(schemaSupplier, node)) {
                    validSchemaSupplier = schemaSupplier;
                }
            }

            return validSchemaSupplier;
        }

    }

    public static Json.Validate validate(JsonNode jsonNode) {
        return new Json.Validate(jsonNode);
    }

    public static JsonNode json(String text) throws JsonParseException, UnsupportedEncodingException, IOException {
        return JsonUtil.parseText(text);
    }

    public static JsonNode json(URL url) throws IOException {
        String text = Resources.asCharSource(url, Charsets.UTF_8).read();
        return json(text);
    }

    public static JsonSchema schema(URL url) throws ProcessingException, IOException {
        return schema(json(url));
    }

    public static JsonSchema schema(String text) throws JsonParseException, UnsupportedEncodingException, IOException, ProcessingException {
        return JsonSchemaUtil.parseText(text);
    }

    public static JsonSchema schema(JsonNode jsonNode) throws ProcessingException {
        return JsonSchemaUtil.parseNode(jsonNode);
    }

    public static <T extends Enum<T> & Supplier<JsonSchema>> Json.WhichSchemaSupplier<T> which(EnumSet<T> enums) {
        return new Json.WhichSchemaSupplier<T>(enums);
    }

    public static <T extends Enum<T> & Supplier<JsonSchema>> Json.WhichSchemaSupplier<T> which(Class<T> enumClass) {
        return new Json.WhichSchemaSupplier<T>(EnumSet.allOf(enumClass));
    }

    public static Json.Select select(JsonPath path) {
        return new Json.Select(path);
    }

    public static JsonPath path(String expression) {
        JsonPath path = JsonPath.compile(expression);
        return path;
    }

}
