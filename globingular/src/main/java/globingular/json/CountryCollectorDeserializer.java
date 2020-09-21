package globingular.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.type.TypeFactory;
import globingular.core.Country;
import globingular.core.CountryCollector;

public class CountryCollectorDeserializer extends JsonDeserializer<CountryCollector> {

    @Override
    public CountryCollector deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        ArrayNode arr = ((ArrayNode) node.get("VisitedCountries"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new CountryCollectorModule());
        List<Country> visitedCountries = new ArrayList<>();

        for (JsonNode country : arr) {
            visitedCountries.add(mapper.convertValue(country, Country.class));
        }

        return new CountryCollector(visitedCountries.toArray(new Country[]{}));
    }

}
