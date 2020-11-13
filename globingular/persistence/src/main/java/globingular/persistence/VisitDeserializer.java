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
        Map<String, Object> injectedMap = (Map<String, Object>)
                ctxt.findInjectableValue(PersistenceHandler.INJECTED_MAP, null, null);
        if (injectedMap.containsKey(PersistenceHandler.INJECTED_MAP_WORLD)) {
            world = (World) injectedMap.get(PersistenceHandler.INJECTED_MAP_WORLD);
        } else {
            throw new IllegalStateException("Visit.class must be deserialized with an injected world.");
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
