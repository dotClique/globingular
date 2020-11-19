package globingular.persistence;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.core.World;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>PersistenceHandler handles file reading and writing from JSON
 * and defines paths for saving CountryCollector-state, app-state,
 * in addition to defining a sample-file to use for the CountryCollector.
 * 
 * A PersistenceHandler class has methods for:
 * <ul>
 * <li>getting an object mapper</li>
 * <li>loading the state in a file to a CountryCollector</li>
 * <li>saving the instance of a CountryCollector to file</li>
 * </ul>
 * </p>
 */

public class PersistenceHandler {

    /**
     * Define Path to the apps datafolder, used for saving app-state.
     */
    private static final Path DATA_FOLDER = Paths.get(System.getProperty("user.home"), "globingular");
    /**
     * Define default username, used as path to Json-file used for saving CountryCollector-state.
     * TODO: Should only be public as long as client side multi-user setup hasn't been implemented!
     */
    public static final String DEFAULT_USERNAME = "countrycollector";

    /**
     * Define which file to get standard world map {@link World} from.
     */
    private static final String DEFAULT_WORLD = "/json/sampleWorld.json";
    /**
     * Name of the default world defined in the apps resources.
     */
    private static final String DEFAULT_WORLD_NAME = "Earth";
    /**
     * Define which sample-file to use for {@link CountryCollector}.
     */
    private static final String SAMPLE_COLLECTOR = "/json/sampleCollector.json";

    /**
     * Static key used for injecting and retrieving a map used for deserialization.
     * The map allows for values to be added in context for further deserialization.
     */
    public static final String INJECTED_MAP = "_globingular_map";
    /**
     * Static key used for storing a reference to a {@link World} used for further deserialization.
     */
    public static final String INJECTED_MAP_WORLD = "_globingular_world";
    /**
     * Static key used for storing a reference to this {@link PersistenceHandler} in an InjectedMap.
     */
    public static final String INJECTED_MAP_PERSISTENCE = "_globingular_persistence";

    /**
     * ObjectMapper used for serialization and deserialization. Contains registered
     * modules for correct (de)serialization.
     */
    private final ObjectMapper objectMapper = getObjectMapper();

    /**
     * Map containing default worlds shipped as part of the application.
     */
    private final Map<String, World> defaultWorlds;

    /**
     * Initialize a new PersistenceHandler with default parameters.
     */
    public PersistenceHandler() {
        World world = loadDefaultWorld();
        defaultWorlds = new HashMap<>();
        defaultWorlds.put(DEFAULT_WORLD_NAME, world);
    }

    /**
     * Get a valid objectMapper-instance with the the correct modules registered,
     * and with a Map injected (using key {@link #INJECTED_MAP}) in its {@link InjectableValues},
     * containing a reference to this {@link PersistenceHandler}.
     * A new {@link ObjectMapper} with a "clean" map is generated each time,
     * ensuring that state is not shared between unrelated (de)serializations.
     * This Map can be used for sharing state downwards when (de)serializing objects.
     * Particularly useful for deserializing a tree of objects in need of sharing certain context,
     * e.g. which {@link World} the {@link Visit}s references.
     * 
     * @return an objectMapper instance
     */
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new CountryCollectorModule());

        // Create a new Map and store a reference to this PersistenceHandler
        Map<String, Object> injectedMap = new HashMap<String, Object>();
        injectedMap.put(PersistenceHandler.INJECTED_MAP_PERSISTENCE, this);

        // Create a new InjectableValues-instance with the injectedMap and add to the mapper
        InjectableValues.Std injectableValues = new InjectableValues.Std();
        injectableValues.addValue(PersistenceHandler.INJECTED_MAP, injectedMap);
        mapper.setInjectableValues(injectableValues);

        return mapper;
    }

    /**
     * Load a default CountryCollector-state.
     *
     * @return A CountryCollector instance containing data loaded from sample file.
     */
    public CountryCollector loadCountryCollector() {
        CountryCollector countryCollector = new CountryCollector(getDefaultWorld());
        try (InputStream in = getClass().getResourceAsStream(SAMPLE_COLLECTOR)) {
            countryCollector = objectMapper.readValue(in, CountryCollector.class);
        } catch (IOException err) {
            // Catch an print if exception
            err.printStackTrace();
        }
        return countryCollector;
    }

    /**
     * Load a CountryCollector-state from file.
     *
     * @param username The username to retrieve a countryCollector for (aka. filename).
     *                 If null is given, default filename is used.
     * @return         A CountryCollector instance containing data loaded from file.
     */
    public CountryCollector loadCountryCollector(final String username) {
        CountryCollector countryCollector = null;
        File file = pathFromUsername(username, DEFAULT_USERNAME).toFile();
        if (file.isFile()) {
            try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
                countryCollector = objectMapper.readValue(in, CountryCollector.class);
            } catch (IOException e) {
                // Catch and print if exception
                e.printStackTrace();
            }
        }
        return countryCollector;
    }

    /**
     * Return a default world defined in the apps resources.
     * 
     * @return The default world-instance
     */
    public World getDefaultWorld() {
        return this.getDefaultWorld(DEFAULT_WORLD_NAME);
    }

    /**
     * If {@code worldName} is a default world, retrieve it. Returns {@code null} if not.
     * 
     * @param worldName The world name to check if is a default world
     * @return Return the World-instance for the world name, or {@code null} if not default
     */
    public World getDefaultWorld(final String worldName) {
        return this.getDefaultWorldOr(worldName, null);
    }

    /**
     * If {@code worldName} is a default world, retrieve it. Returns {@code or} if not.
     * 
     * @param worldName The world name to check if is a default world
     * @param or The world to return if not a default world
     * @return Return the World-instance for the world name, or {@code or} if not default
     */
    public World getDefaultWorldOr(final String worldName, final World or) {
        return this.defaultWorlds.getOrDefault(worldName, or);
    }

    /**
     * Set PersistenceHandler to autosave changes in a CountryCollector.
     * 
     * @param username         The username to save state for (and use as filename).
     *                         If null is given, default filename is used.
     * @param countryCollector The CountryCollector to autosave
     * 
     * @throws IllegalArgumentException If filename is invalid: {@link GlobingularModule#isUsernameValid(String)}.
     */
    public void setAutosave(final String username, final CountryCollector countryCollector)
            throws IllegalArgumentException {
        if (!GlobingularModule.isUsernameValid(username)) {
            throw new IllegalArgumentException("Invalid username: " + username);
        }
        countryCollector.addListener(event -> {
            try {
                this.saveState(username, countryCollector);
            } catch (IOException e) {
                // TODO: Like this the user will not be notified about the exception :/
                e.printStackTrace();
            }
        });
    }

    /**
     * Load default {@link World} from file.
     * 
     * @return Default World-instance.
     */
    private World loadDefaultWorld() {
        World world = new World();
        try (InputStream in = getClass().getResourceAsStream(DEFAULT_WORLD)) {
            world = objectMapper.readValue(in, World.class);
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
     * @param username         The username to save state for (and use as filename).
     *                         If null is given, default filename is used instead.
     * @param countryCollector The CountryCollector instance to save.
     *                         Will delete file instead if countryCollector is null.
     * @return                 True if successfully saved
     * 
     * @throws IllegalArgumentException If filename is not alphanumeric
     * @throws IOException              If exception is thrown upon saving
     */
    public boolean saveState(final String username, final CountryCollector countryCollector)
            throws IllegalArgumentException, IOException {
        // Make sure necessary directories exist before trying to write files
        // May throw IOException
        Files.createDirectories(DATA_FOLDER);
        // If countryCollector is null, delete file
        if (countryCollector == null) {
            return deleteState(username);
        }
        // Try to save
        // Caught exception is thrown. Using try-with to ensure closing of writer.
        try (Writer out = Files.newBufferedWriter(pathFromUsername(username, DEFAULT_USERNAME))) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, countryCollector);
            return true;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Delete saved state for the given username.
     * 
     * @param username The username to delete
     * @return         True if successfully deleted
     * 
     * @throws IllegalArgumentException If filename is not alphanumeric
     */
    public boolean deleteState(final String username) throws IllegalArgumentException {
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
        if (!GlobingularModule.isUsernameValid(username)) {
            throw new IllegalArgumentException("Filename must be alphanumeric!");
        }
        return DATA_FOLDER.resolve(username.toLowerCase() + ".json");
    }
}
