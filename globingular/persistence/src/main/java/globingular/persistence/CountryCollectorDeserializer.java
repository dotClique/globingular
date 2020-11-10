package globingular.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.core.World;

import java.io.IOException;
import java.util.Map;

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
     * @throws IOException on general parsing error
     * @throws NullPointerException on missing fields
     */
    @Override
    public CountryCollector deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

        //JsonNode node = p.getCodec().readTree(p);
        ObjectNode node = p.readValueAsTree();
        final World world;

        // Suppress warning, as we know this is fine
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) ctxt.findInjectableValue("_globingular", null, null);
        if (map.containsKey("_globingular_world")) {
            world = (World) map.get("_globingular_world");
        } else {
            // If World is present as a node in the JSON, use it!
            World tmpWorld = node.get("World").traverse(p.getCodec()).readValueAs(World.class);

            PersistenceHandler persitencHandler = (PersistenceHandler)
            ctxt.findInjectableValue("_globingular_persistence", null, null);
            world = persitencHandler.getDefaultWorldOr(tmpWorld.getWorldName(), tmpWorld);

            map.put("_globingular_world", world);
        }

        CountryCollector countryCollector = new CountryCollector(world);

        Visit[] visits = node.get("Visits").traverse(p.getCodec()).readValueAs(Visit[].class);

        for (Visit visit : visits) {
            countryCollector.registerVisit(visit);
        }

        return countryCollector;
    }
}
