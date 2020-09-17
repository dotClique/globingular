package globingular.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.core.Country;

public class CountriesSerializer {
    public static String serializeCountry(Country c) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(c);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
