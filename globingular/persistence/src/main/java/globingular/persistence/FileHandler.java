package globingular.persistence;

import globingular.core.CountryCollector;
import globingular.core.World;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Low-level class handling all direct access to files.
 */
public final class FileHandler {

    /**
     * Define Path to the apps datafolder, used for saving app-state.
     */
    private static final Path DATA_FOLDER = Paths.get(System.getProperty("user.home"), "globingular");
    /**
     * Define Path to Json-file used for saving CountryCollector-state.
     */
    private static final String DEFAULT_COLLECTOR_FILENAME = "countrycollector";
    /**
     * Define which file to get standard world map {@link World} from.
     */
    private static final String DEFAULT_WORLD_FILENAME = "/json/sampleWorld.json";

    /**
     * Private constructor to disallow instantiation.
     */
    private FileHandler() {
    }

    /**
     * Load a CountryCollector-state from file.
     *
     * @param username           The username to retrieve a countryCollector for (aka. filename).
     *                           If null is given, default filename is used.
     * @param persistenceHandler The provider of {@link com.fasterxml.jackson.databind.ObjectMapper}s
     *                           and injected values to use for deserialization.
     * @return A CountryCollector instance containing data loaded from file, or null if failed.
     */
    public static CountryCollector loadCountryCollector(final PersistenceHandler persistenceHandler,
                                                        final String username) {
        CountryCollector countryCollector = null;
        File file = pathFromFilename(username, DEFAULT_COLLECTOR_FILENAME).toFile();
        if (file.isFile()) {
            try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
                countryCollector = persistenceHandler.getObjectMapper().readValue(in, CountryCollector.class);
            } catch (IOException e) {
                // Catch and print if exception
                e.printStackTrace();
            }
        }
        return countryCollector;
    }

    /**
     * Load the predominant default {@link World} from file in the application package.
     * Does not use the injected object mapper to avoid circular dependency,
     * and as injection isn't required for deserializing a World.
     *
     * @return Predominant default World-instance.
     */
    static World loadPredominantDefaultWorld() {
        World world = new World();
        try (InputStream in = PersistenceHandler.class.getResourceAsStream(DEFAULT_WORLD_FILENAME)) {
            world = PersistenceHandler.getUninjectedObjectMapper().readValue(in, World.class);
        } catch (IOException e) {
            // Catch and print if exception
            e.printStackTrace();
        }

        return world;
    }

    /**
     * Save a CountryCollector instance to file.
     *
     * @param persistenceHandler the provider of {@link com.fasterxml.jackson.databind.ObjectMapper}s
     *                           and injected values to use for serialization.
     * @param username         The username to save state for (and use as filename).
     *                         If null is given, default filename is used instead.
     * @param countryCollector The CountryCollector instance to save
     * @return Whether the save succeeded
     * @throws IllegalArgumentException If filename is not alphanumeric
     */
    public static boolean saveCountryCollector(final PersistenceHandler persistenceHandler, final String username,
                                               final CountryCollector countryCollector)
            throws IllegalArgumentException {
        try {
            Files.createDirectories(DATA_FOLDER);
        } catch (IOException e) {
            // Catch and print if exception
            e.printStackTrace();
            return false;
        }
        try (Writer out = Files.newBufferedWriter(pathFromFilename(username, DEFAULT_COLLECTOR_FILENAME))) {
            persistenceHandler.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, countryCollector);
            return true;
        } catch (IOException e) {
            // Catch and print if exception
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates filename and returns a valid path to json-file.
     * Filename is valid if alphanumeric.
     *
     * @param filename        The filename to validate
     * @param defaultFilename The filename to return if the one given is null
     * @return                A valid path to the given filename (in lowercase),
     *                        or to defaultFilename if filename is null
     *
     * @throws IllegalArgumentException If filename is invalid and not null.
     *                                  Or if both filename and defaultFilename are null.
     */
    private static Path pathFromFilename(final String filename, final String defaultFilename)
            throws IllegalArgumentException {
        // If filename is null, try to use defaultFilename. If both are null, throw exception.
        if (filename == null) {
            if (defaultFilename == null) {
                throw new IllegalArgumentException("Both filename and defaultFilename can't be null!");
            }
            return pathFromFilename(defaultFilename, null);
        }
        // If not alphanumeric, throw exception.
        if (!filename.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("Filename must be alphanumeric!");
        }
        return DATA_FOLDER.resolve(filename.toLowerCase() + ".json");
    }
}
