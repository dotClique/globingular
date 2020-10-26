package globingular.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import globingular.core.CountryCollector;
import globingular.core.Visit;
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
     * @param p a JsonParser
     * @param ctxt a context for the deserialization
     * @return a CountryCollector object
     * @throws IOException
     */
    @Override
    public CountryCollector deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {

        // Get World instance defined higher in the context tree.
        World world = (World) ctxt.findInjectableValue("_globingular_map_world", null, null);
        CountryCollector countryCollector = new CountryCollector(world);

        //JsonNode node = p.getCodec().readTree(p);
        ObjectNode node = p.readValueAsTree();

        JsonParser visitParser = node.get("Visits").traverse(p.getCodec());
        Visit[] visits = p.getCodec().readValue(visitParser, Visit[].class);

        for (Visit visit : visits) {
            countryCollector.registerVisit(visit);
        }

        return countryCollector;
    }
}
