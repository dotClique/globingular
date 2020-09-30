package globingular.json;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import globingular.core.CountryCollector;

public class PersistenceHandler {

    /**
     * Define Path to Json-file used for saving CountryCollector-state.
     */
    private static final Path FILE_COLLECTOR = Paths.get(System.getProperty("user.home"), "temp", "globingular",
            "countryCollector.json");

    /**
     * Define Path to the apps datafolder, used for saving app-state.
     */
    private static final Path DATA_FOLDER = FILE_COLLECTOR.getParent();

    /**
     * Define which sample-file to use for CountryCollector.
     */
    private static final String SAMPLE_COLLECTOR = "/json/sampleCollector.json";

    /**
     * ObjectMapper used for serialization and deserialization. Contains registered
     * modules for correct (de)serialization.
     */
    private ObjectMapper objectMapper;

    /**
     * Get a valid instance of objectmapper.
     * 
     * @return an objectmapper instance
     */
    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new CountryCollectorModule());
        }
        return objectMapper;
    }

    /**
     * Load a CountryCollector-state from file.
     * 
     * @return A CountryCollector instance containing data loaded from file
     */
    public CountryCollector loadState() {
        CountryCollector countryCollector = null;
        try (InputStream in = new BufferedInputStream(new FileInputStream(FILE_COLLECTOR.toFile()))) {
            countryCollector = getObjectMapper().readValue(in, CountryCollector.class);
        } catch (FileNotFoundException e) {
            try (InputStream in = getClass().getResourceAsStream(SAMPLE_COLLECTOR)) {
                countryCollector = getObjectMapper().readValue(in, CountryCollector.class);
            } catch (IOException err) {
                err.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryCollector;
    }

    /**
     * Save a CountryCollector instance to file.
     * 
     * @param countryCollector The CountryCollector instance to save
     */
    public void saveState(final CountryCollector countryCollector) {
        try {
            Files.createDirectories(DATA_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Writer out = Files.newBufferedWriter(FILE_COLLECTOR)) {
            getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, countryCollector);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
