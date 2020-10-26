package globingular.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import globingular.core.Country;
import globingular.core.Visit;
import globingular.core.World;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Deserializer for {@link Country} objects.
 */
public class VisitDeserializer extends JsonDeserializer<Visit> {

    /**
     * Deserilize a Visit from JSON.
     * @param p The relevant JsonParser
     * @param ctxt The current parsing context
     * @return The deserialized Country
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
        String arrivalText = visitNode.get("arrival").asText();
        String departureText = visitNode.get("departure").asText();

        Country country = world.getCountryFromCode(countryCode);
        LocalDateTime arrival = LocalDateTime.parse(arrivalText);
        LocalDateTime departure = LocalDateTime.parse(departureText);

        return new Visit(country, arrival, departure);
    }
}
