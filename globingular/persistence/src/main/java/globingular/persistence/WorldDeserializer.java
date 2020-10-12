package globingular.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import globingular.core.Country;
import globingular.core.World;

import java.io.IOException;

/**
 * Deserializer for {@link World} objects.
 */
public class WorldDeserializer extends JsonDeserializer<World> {

    /**
     * Deserialize a {@link World} from JSON.
     *
     * @param p The current JsonParser
     * @param ctxt The current deserialization context
     * @return The deserialized World
     * @throws IOException On general parsing error
     * @throws NullPointerException on missing field
     */
    public World deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {
        ObjectNode o = p.readValueAsTree();

        JsonParser countryParser = o.get("Countries").traverse(p.getCodec());
        Country[] countries = p.getCodec().readValue(countryParser, Country[].class);
        return new World(countries);
    }
}
