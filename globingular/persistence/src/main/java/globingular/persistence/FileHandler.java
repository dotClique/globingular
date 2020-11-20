package globingular.persistence;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
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
    public static final Path DATA_FOLDER = Paths.get(System.getProperty("user.home"),
            System.getProperty("globingular.datafolder", "globingular"));
    /**
     * Define default username, used as path to Json-file used for saving CountryCollector-state.
     */
    private static final String DEFAULT_USERNAME = "default";
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
        File file = pathFromUsername(username, DEFAULT_USERNAME).toFile();
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
     * Save a {@link CountryCollector} instance to file.
     * If countryCollector is null, will try to delete instead.
     *
     * @param persistenceHandler The provider of {@link com.fasterxml.jackson.databind.ObjectMapper}s
     *                           and injected values to use for serialization.
     * @param username         The username to save state for (and use as filename).
     *                         If null is given, default filename is used instead.
     * @param countryCollector The CountryCollector instance to save.
     *                         Will delete file instead if countryCollector is null.
     * @return Whether the save succeeded
     *
     * @throws IllegalArgumentException If filename is not alphanumeric
     * @throws IOException              If exception is thrown upon saving
     */
    public static boolean saveCountryCollector(final PersistenceHandler persistenceHandler, final String username,
            final CountryCollector countryCollector) throws IllegalArgumentException, IOException {
        // Make sure necessary directories exist before trying to write files
        // May throw IOException
        Files.createDirectories(DATA_FOLDER);
        // If countryCollector is null, delete file
        if (countryCollector == null) {
            return deleteCountryCollector(username);
        }
        // Try to save
        // Caught exception is thrown. Using try-with to ensure closing of writer.
        try (Writer out = Files.newBufferedWriter(pathFromUsername(username, DEFAULT_USERNAME))) {
            persistenceHandler.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, countryCollector);
            return true;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Delete saved {@link CountryCollector} for the given username.
     *
     * @param username The username whose CountryCollector to delete
     * @return         True if successfully deleted
     *
     * @throws IllegalArgumentException If filename is not alphanumeric
     */
    private static boolean deleteCountryCollector(final String username) throws IllegalArgumentException {
        File file = pathFromUsername(username, DEFAULT_USERNAME).toFile();
        if (file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Validates username and returns a valid path to json-file, using username as filename.
     * Validated by {@link GlobingularModule#isUsernameValid(String)}.
     *
     * @param username        The username to validate and use for saving
     * @param defaultFilename The filename to use if the one given is null
     * @return                A valid path using the given username (in lowercase),
     *                        or using defaultFilename if username is null
     *
     * @throws IllegalArgumentException If username is invalid and not null.
     *                                  Or if both username and defaultFilename are null.
     */
    private static Path pathFromUsername(final String username, final String defaultFilename)
            throws IllegalArgumentException {
        // If username is null, try to use defaultFilename. If both are null, throw exception.
        if (username == null) {
            if (defaultFilename == null) {
                throw new IllegalArgumentException("Both username and defaultFilename can't be null!");
            }
            return pathFromUsername(defaultFilename, null);
        }
        // If not valid, throw exception
        if (!GlobingularModule.isUsernameValid(username.toLowerCase())) {
            throw new IllegalArgumentException("Filename must be alphanumeric: " + username);
        }
        return DATA_FOLDER.resolve(username.toLowerCase() + ".json");
    }
}
