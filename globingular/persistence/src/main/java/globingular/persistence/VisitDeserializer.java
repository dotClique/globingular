package globingular.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import globingular.core.Country;
import globingular.core.Visit;
import globingular.core.World;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Deserializer for {@link Visit} objects.
 */
public class VisitDeserializer extends JsonDeserializer<Visit> {

    /**
     * Deserilize a Visit from JSON.
     * @param p The relevant JsonParser
     * @param ctxt The current parsing context
     * @return The deserialized Visit
     * @throws IOException on general parsing error
     * @throws NullPointerException on missing fields
     */
    @Override
    public Visit deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

        ObjectNode node = p.readValueAsTree();
        final World world;

        // Suppress warning, as we know this is fine
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) ctxt.findInjectableValue("_globingular", null, null);
        if (map.containsKey("_globingular_world")) {
            world = (World) map.get("_globingular_world");
        } else {
            if (node.has("World")) {
                // If World is present as a node in the JSON, use it!
                JsonParser worldParser = node.get("World").traverse(p.getCodec());
                world = p.getCodec().readValue(worldParser, World.class);
            } else {
                // Else load default World
                try (InputStream in = getClass().getResourceAsStream("/json/sampleWorld.json")) {
                    world = (new PersistenceHandler()).getObjectMapper().readValue(in, World.class);
                } catch (IOException err) {
                    throw err;
                }
            }
            map.put("_globingular_world", world);
        }

        String countryCode = node.get("countryCode").asText();
        JsonNode arrivalNode = node.get("arrival");
        JsonNode departureNode = node.get("departure");

        Country country = world.getCountryFromCode(countryCode);
        LocalDateTime arrival = arrivalNode.isNull() ? null : LocalDateTime.parse(arrivalNode.asText());
        LocalDateTime departure = departureNode.isNull() ? null : LocalDateTime.parse(departureNode.asText());

        return new Visit(country, arrival, departure);
    }
}
