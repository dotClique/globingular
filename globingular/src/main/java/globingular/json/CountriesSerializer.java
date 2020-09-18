package globingular.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.core.Country;

public class CountriesSerializer {
    public static String serializeCountry(Country c) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(c);
    }
}
