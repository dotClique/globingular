package globingular.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import globingular.core.CountryCollector;

public class CountryCollectorDeserializer extends JsonDeserializer<CountryCollector> {

    /**
     * Deserialize a CountryCollector-object from JSON using JsonParser.
     */
    @Override
    public CountryCollector deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        ArrayNode arr = ((ArrayNode) node.get("VisitedCountries"));
        String[] countries = new String[arr.size()];

        for (int i = 0; i < arr.size(); i++) {
            countries[i] = arr.get(i).asText();
        }

        return new CountryCollector(countries);
    }

}
