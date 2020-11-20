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

class CountryCollectorDeserializer extends JsonDeserializer<CountryCollector> {

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

        // Retrieve World from Json
        World tmpWorld = node.get("World").traverse(p.getCodec()).readValueAs(World.class);

        // Check if tmpWorld is a defaultWorld, in which case retrieve server-version from persistenceHandler
        PersistenceHandler persistenceHandler = (PersistenceHandler)
                injectedMap.get(PersistenceHandler.INJECTED_MAP_PERSISTENCE);
        world = persistenceHandler.getDefaultWorldOr(tmpWorld.getWorldName(), tmpWorld);

        // Insert world into injectedMap for use in further deserialization
        injectedMap.put(PersistenceHandler.INJECTED_MAP_WORLD, world);

        // Parse all visits, using the injected world
        Visit[] visits = node.get("Visits").traverse(p.getCodec()).readValueAs(Visit[].class);

        // Create CountryCollector with the parsed World, and register visits
        CountryCollector countryCollector = new CountryCollector(world);
        for (Visit visit : visits) {
            countryCollector.registerVisit(visit);
        }

        return countryCollector;
    }
}
