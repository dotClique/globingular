package globingular.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import globingular.core.CountryCollector;
import globingular.core.World;

import java.io.IOException;

/**
 * <p>CountryCollectorDeserializer deserializes a CountryCollector-object, using
 * the only method in the class, deserialize(p, ctxt),
 * to do this.</p>
 */

public class CountryCollectorDeserializer extends JsonDeserializer<CountryCollector> {

    /**
     * Deserialize a CountryCollector-object from JSON using JsonParser.
     * Requires the target World as an injectable value "_globingular_map_world" in the context.
     * 
     * @param c a JsonParser
     * @param ctxt a context for the deserialization
     * @return a CountryCollector object
     * @throws IOException
     */
    @Override
    public CountryCollector deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        // Get World instance defined higher in the context tree.
        World world = (World) ctxt.findInjectableValue("_globingular_map_world", null, null);
        CountryCollector countryCollector = new CountryCollector(world);

        ArrayNode arr = ((ArrayNode) node.get("VisitedCountries"));

        for (JsonNode country : arr) {
            countryCollector.setVisited(world.getCountryFromCode(country.asText()));
        }

        return countryCollector;
    }

}
