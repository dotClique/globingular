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

    static final private Path FILE_COLLECTOR = Paths.get(System.getProperty("user.home"), "temp", "globingular", "countryCollector.json");
    static final private Path DATA_FOLDER = FILE_COLLECTOR.getParent();
    static final private String SAMPLE_COLLECTOR = "/json/sampleCollector.json";

    private ObjectMapper objectMapper;

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new CountryCollectorModule());
        }
        return objectMapper;
    }

    public CountryCollector loadState() {
        CountryCollector countryCollector = null;
        try (InputStream in = new BufferedInputStream(new FileInputStream(FILE_COLLECTOR.toFile()))) {
            countryCollector = this.getObjectMapper().readValue(in, CountryCollector.class);
        } catch (FileNotFoundException e) {
            try (InputStream in = getClass().getResourceAsStream(SAMPLE_COLLECTOR)) {
                countryCollector = this.getObjectMapper().readValue(in, CountryCollector.class);
            } catch (IOException err) {
                err.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryCollector;
    }

    public CountryCollector saveState(CountryCollector countryCollector) {
        try {
            Files.createDirectories(DATA_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Writer out = Files.newBufferedWriter(FILE_COLLECTOR)) {
            this.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, countryCollector);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryCollector;
    }
}
