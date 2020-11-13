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
     * Deserialize a {@link CountryCollector}-object from JSON using JsonParser.
     * Requires the target {@link World} in the injected map ({@link PersistenceHandler#INJECTED_MAP}) in the context
     * or defined in the JSON itself.
     * 
     * @param p a JsonParser
     * @param ctxt a context for the deserialization
     * @return a {@link CountryCollector} object
     * @throws IOException on general parsing error
     * @throws NullPointerException on missing fields
     */
    @Override
    public CountryCollector deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

        ObjectNode node = p.readValueAsTree();
        final World world;

        // Suppress warning. This should be fine, and if it's not we want it to throw an exception anyway.
        @SuppressWarnings("unchecked")
        Map<String, Object> injectedMap = (Map<String, Object>)
                ctxt.findInjectableValue(PersistenceHandler.INJECTED_MAP, null, null);
        if (injectedMap.containsKey(PersistenceHandler.INJECTED_MAP_WORLD)) {
            world = (World) injectedMap.get(PersistenceHandler.INJECTED_MAP_WORLD);
        } else {
            // If World is present as a node in the JSON, use it!
            World tmpWorld = node.get("World").traverse(p.getCodec()).readValueAs(World.class);

            PersistenceHandler persitencHandler = (PersistenceHandler)
                    injectedMap.get(PersistenceHandler.INJECTED_MAP_PERSISTENCE);
            world = persitencHandler.getDefaultWorldOr(tmpWorld.getWorldName(), tmpWorld);

            injectedMap.put(PersistenceHandler.INJECTED_MAP_WORLD, world);
        }

        CountryCollector countryCollector = new CountryCollector(world);

        Visit[] visits = node.get("Visits").traverse(p.getCodec()).readValueAs(Visit[].class);

        for (Visit visit : visits) {
            countryCollector.registerVisit(visit);
        }

        return countryCollector;
    }
}
