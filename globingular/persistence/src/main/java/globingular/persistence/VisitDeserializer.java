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
import java.time.LocalDateTime;

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
    public Visit deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {

        // Get World instance defined higher in the context tree.
        World world = (World) ctxt.findInjectableValue("_globingular_map_world", null, null);

        ObjectNode visitNode = p.readValueAsTree();
        String countryCode = visitNode.get("countryCode").asText();
        JsonNode arrivalNode = visitNode.get("arrival");
        JsonNode departureNode = visitNode.get("departure");

        Country country = world.getCountryFromCode(countryCode);
        LocalDateTime arrival = arrivalNode.isNull() ? null : LocalDateTime.parse(arrivalNode.asText());
        LocalDateTime departure = departureNode.isNull() ? null : LocalDateTime.parse(departureNode.asText());

        return new Visit(country, arrival, departure);
    }
}
