package globingular.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.core.Country;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CountriesSerializer {
    public static String serializeCountry(Country c) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(c);
    }

    public void exportAll(Collection<Country> countries) {
        Path FILE_COUNTRIES = Paths.get(System.getProperty("user.home"), "temp", "globingular", "countryData.json");
        try {
            Files.createDirectories(FILE_COUNTRIES);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Writer out = Files.newBufferedWriter(FILE_COUNTRIES)) {
            new ObjectMapper().registerModule(new CountryCollectorModule()).writeValue(out, countries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
