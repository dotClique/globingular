package globingular.persistence;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import globingular.core.CountryCollector;
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
    private static final Path DATA_FOLDER = Paths.get(System.getProperty("user.home"), "temp", "globingular");
    /**
     * Define Path to Json-file used for saving CountryCollector-state.
     */
    private static final String DEFAULT_COLLECTOR_FILENAME = "countrycollector";

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
     * @param filename The filename to retrieve a countryCollector from.
     *                 If null is given, default filename is used.
     * @return         A CountryCollector instance containing data loaded from file.
     */
    public CountryCollector loadCountryCollector(final String filename) {
        CountryCollector countryCollector = null;
        File file = pathFromFilename(filename, DEFAULT_COLLECTOR_FILENAME).toFile();
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
     * @param filename         The filename to save as.
     *                         If null is given, default filename is used.
     * @param countryCollector The CountryCollector to autosave
     * 
     * @throws IllegalArgumentException If filename is not alphanumeric
     */
    public void setAutosave(final String filename, final CountryCollector countryCollector)
            throws IllegalArgumentException {
        countryCollector.addListener(event -> {
            // saveState uses default if filename is null, and throws IllegalArgumentException if invalid
            this.saveState(filename, countryCollector);
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
     * Save a CountryCollector instance to file.
     *
     * @param filename         The filename to save as.
     *                         If null is given, default filename is used.
     * @param countryCollector The CountryCollector instance to save
     * 
     * @throws IllegalArgumentException If filename is not alphanumeric
     */
    public void saveState(final String filename, final CountryCollector countryCollector)
            throws IllegalArgumentException {
        try {
            Files.createDirectories(DATA_FOLDER);
        } catch (IOException e) {
            // Catch and print if exception
            e.printStackTrace();
        }
        try (Writer out = Files.newBufferedWriter(pathFromFilename(filename, DEFAULT_COLLECTOR_FILENAME))) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, countryCollector);
        } catch (IOException e) {
            // Catch and print if exception
            e.printStackTrace();
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
