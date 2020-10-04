package globingular.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import globingular.core.Country;
import globingular.core.Province;

import java.io.IOException;

public class CountryDeserializer extends JsonDeserializer<Country> {

    /**
     * Deserilize a Country from JSON.
     * @param p The relevant JsonParser
     * @param ctxt The current parsing context
     * @return The deserialized Country
     * @throws IOException on general parsing error
     * @throws NullPointerException on missing fields
     */
    @Override
    public Country deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {
        ObjectNode countryNode = p.readValueAsTree();
        String countryCode = countryNode.get("countryCode").asText();
        String name = countryNode.get("name").asText();
        String longname = countryNode.get("longname").asText();
        String sovereignty = countryNode.get("sovereignty").asText();
        String region = countryNode.get("region").asText();
        long population = countryNode.get("population").asLong();

        return new Country(countryCode, name, longname, sovereignty, region, population, new Province[0]);
    }
}
