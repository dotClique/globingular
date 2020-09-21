package globingular.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import globingular.core.Country;
import globingular.core.Province;

import java.io.IOException;

public class CountryDeserializer extends JsonDeserializer<Country> {

    @Override
    public Country deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        String countryCode = node.get("countryCode").asText();
        String name = node.get("name").asText();
        String longname = node.get("longname").asText();
        String sovereignty = node.get("sovereignty").asText();
        String region = node.get("region").asText();
        long population = node.get("population").asLong();

        return new Country(countryCode, name, longname, sovereignty, region, population, new Province[0]);
    }

}

