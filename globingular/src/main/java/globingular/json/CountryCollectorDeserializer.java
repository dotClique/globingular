package globingular.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import globingular.core.CountryCollector;

public class CountryCollectorDeserializer extends JsonDeserializer<CountryCollector> {

    @Override
    public CountryCollector deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode node = p.getCodec().readTree(p);

        ArrayNode arr = ((ArrayNode) node.get("VisitedCountries"));
        CountryCollector c = new CountryCollector();
        
        for (int i = 0; i < arr.size(); i++) {
            c.setVisited(arr.get(i).asText());
        }
        
        return c;
    }

}
