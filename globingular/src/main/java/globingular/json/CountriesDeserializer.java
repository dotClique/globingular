package globingular.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import globingular.core.Country;

public class CountriesDeserializer {

    public List<Country> importAll() {
        List<Country> countries;
        try {
            ObjectMapper mapper = new ObjectMapper(); // create once, reuse
            mapper.registerModule(new CountryCollectorModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            InputStream is = CountriesDeserializer.class.getResourceAsStream("/json/countryData.json");
            countries = mapper.readValue(is, mapper.getTypeFactory().constructCollectionType(List.class, Country.class));


        } catch (IOException e) {
            e.printStackTrace();
            countries = new ArrayList<>();

        }
        return countries;
    }
}
