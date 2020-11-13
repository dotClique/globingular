package globingular.persistence;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import globingular.core.CountryCollector;
import globingular.core.World;
import globingular.core.Country;
import javafx.collections.SetChangeListener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
     * Define Path to Json-file used for saving CountryCollector-state.
     */
    private static final Path FILE_COLLECTOR = Paths.get(System.getProperty("user.home"), "temp", "globingular",
                                                         "countryCollector.json");

    /**
     * Define Path to the apps datafolder, used for saving app-state.
     */
    private static final Path DATA_FOLDER = FILE_COLLECTOR.getParent();

    /**
     * Define which file to get standard world map {@link World} from.
     */
    private static final String FILE_MAP_WORLD = "/json/sampleWorld.json";

    /**
     * Define which sample-file to use for CountryCollector.
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
        World world = loadMapWorld();
        defaultWorlds = new HashMap<>();
        defaultWorlds.put("Earth", world);
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
     * Load a CountryCollector-state from file.
     *
     * @return A CountryCollector instance containing data loaded from file
     */
    public CountryCollector loadCountryCollector() {
        World world = loadWorld();

        CountryCollector countryCollector = new CountryCollector(world);
        try (InputStream in = new BufferedInputStream(new FileInputStream(FILE_COLLECTOR.toFile()))) {
            countryCollector = objectMapper.readValue(in, CountryCollector.class);
        } catch (FileNotFoundException e) {
            try (InputStream in = getClass().getResourceAsStream(SAMPLE_COLLECTOR)) {
                countryCollector = objectMapper.readValue(in, CountryCollector.class);
            } catch (IOException err) {
                err.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryCollector;
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
     * @param countryCollector The CountryCollector to autosave
     */
    public void setAutosave(final CountryCollector countryCollector) {
        countryCollector.visitedCountriesProperty()
                        .addListener((SetChangeListener<? super Country>) e -> {
                            this.saveState(countryCollector);
                        });
    }

    private World loadWorld() {
        World world = new World();
        try (InputStream in = getClass().getResourceAsStream(FILE_MAP_WORLD)) {
            world = objectMapper.readValue(in, World.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return world;
    }

    /**
     * Save a CountryCollector instance to file.
     *
     * @param countryCollector The CountryCollector instance to save
     */
    private void saveState(final CountryCollector countryCollector) {
        try {
            Files.createDirectories(DATA_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Writer out = Files.newBufferedWriter(FILE_COLLECTOR)) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, countryCollector);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
