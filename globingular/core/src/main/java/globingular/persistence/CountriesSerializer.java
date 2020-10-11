package globingular.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.core.Country;

public final class CountriesSerializer {

    private CountriesSerializer() { }

    /**
     * Return serialized string of Country.
     * 
     * @param c The Country object to serialize
     * @return A serialized string representation. Stable interface
     * @throws JsonProcessingException
     */
    public static String serializeCountry(final Country c) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(c);
    }
}
