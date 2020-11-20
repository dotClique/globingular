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
import java.time.LocalDate;
import java.util.Map;

/**
 * Deserializer for {@link Visit} objects.
 */
class VisitDeserializer extends JsonDeserializer<Visit> {

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

        // Suppress warning. This should be fine, and if it's not we want it to throw an exception anyway.
        @SuppressWarnings("unchecked")
        Map<String, Object> injectedMap = (Map<String, Object>)
                ctxt.findInjectableValue(PersistenceHandler.INJECTED_MAP, null, null);
        if (injectedMap.containsKey(PersistenceHandler.INJECTED_MAP_WORLD)) {
            world = (World) injectedMap.get(PersistenceHandler.INJECTED_MAP_WORLD);
        } else {
            // If there's no world in injectedMap, the current deserialization is not part of a larger process,
            // and so we will create a "dummy" country and leave it up to the retrieving code to verify and repair.
            world = null;
        }

        String countryCode = node.get("countryCode").asText();
        JsonNode arrivalNode = node.get("arrival");
        JsonNode departureNode = node.get("departure");

        // If World is null, create a new "dummy" Country using countryCode as name
        Country country = world == null ? new Country(countryCode, countryCode) : world.getCountryFromCode(countryCode);
        LocalDate arrival = arrivalNode.isNull() ? null : LocalDate.parse(arrivalNode.asText());
        LocalDate departure = departureNode.isNull() ? null : LocalDate.parse(departureNode.asText());

        return new Visit(country, arrival, departure);
    }
}
