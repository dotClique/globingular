package it1901.gr2002.globingular.persistence;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import it1901.gr2002.globingular.core.CountryCollector;

public class CountryCollectorDeserializer extends JsonDeserializer<CountryCollector> {

    /**
     * Deserialize a CountryCollector-object from JSON using JsonParser.
     */
    @Override
    public CountryCollector deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        ArrayNode arr = ((ArrayNode) node.get("VisitedCountries"));
        CountryCollector c = new CountryCollector();

        for (int i = 0; i < arr.size(); i++) {
            c.setVisited(arr.get(i).asText());
        }

        return c;
    }

}
