package globingular.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import globingular.core.CountryCollector;
import globingular.core.Province;

import java.io.IOException;

public class CountryCollectorDeserializer extends JsonDeserializer<CountryCollector> {

    @Override
    public CountryCollector deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new CountryCollectorModule());

        CountryCollector countryCollector = new CountryCollector();

        JsonNode node = p.getCodec().readTree(p);
        ArrayNode existingCountriesNode = (ArrayNode) node.get("ExistingCountries");

        for (var country : existingCountriesNode) {
            String countryCode = country.get("countryCode").asText();
            String name = country.get("name").asText();
            String longname = country.get("longname").asText();
            String sovereignty = country.get("sovereignty").asText();
            String region = country.get("region").asText();
            long population = country.get("population").asLong();

            countryCollector.new Country(countryCode, name, longname, sovereignty, region, population, new Province[0]);
        }

        ArrayNode arr = ((ArrayNode) node.get("VisitedCountries"));

        for (JsonNode country : arr) {
            countryCollector.setVisited(countryCollector.getCountryFromCode(country.asText()));
        }

        return countryCollector;
    }

}
