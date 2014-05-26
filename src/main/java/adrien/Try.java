package adrien;

import static radium.json.Json.json;
import static radium.json.Json.which;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Resources;

public class Try {

    public static void main(String[] arguments) throws Throwable {
        JsonNode genius = json(Resources.getResource("Genius.json"));

        Schema schema = which(Schema.class).validate(genius);
        switch (schema) {
            case IDENTITY:
                System.out.println("Identity! ");
                break;
            case CAR:
                System.out.println("Car! ");
                break;

            default:
                System.out.println("None... ");
        }

    }

}
