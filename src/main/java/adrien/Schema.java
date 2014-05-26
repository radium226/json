package adrien;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import radium.json.Json;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.google.common.base.Supplier;
import com.google.common.io.Resources;

public enum Schema implements Supplier<JsonSchema> {

    IDENTITY("Identity"), CAR("Car");

    private String schemaName;

    final private static String SUFFIX = ".json";

    Schema(String schemaName) {
        this.schemaName = schemaName;
    }

    public JsonSchema get() {
        String schemaFileName = this.schemaName + SUFFIX;
        URL schemaURL = Resources.getResource(schemaFileName);
        try {
            return Json.schema(schemaURL);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
