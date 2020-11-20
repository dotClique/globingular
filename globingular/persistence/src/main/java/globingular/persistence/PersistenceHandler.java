package globingular.persistence;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import globingular.core.World;

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
     * Map containing default worlds shipped as part of the application.
     */
    private final Map<String, World> defaultWorlds;
    /**
     * The worldName of the predominant default {@link World}.
     */
    private final String predominantDefaultWorldName;

    /**
     * Initialize a new PersistenceHandler with default parameters.
     */
    public PersistenceHandler() {
        World world = FileHandler.loadPredominantDefaultWorld();
        defaultWorlds = new HashMap<>();
        predominantDefaultWorldName = world.getWorldName();
        defaultWorlds.put(predominantDefaultWorldName, world);
    }

    /**
     * Get a valid objectMapper-instance with the the correct modules registered,
     * but without a Map injected (see {@link #getObjectMapper()}.
     *
     * @return an objectMapper instance
     */
    public static ObjectMapper getUninjectedObjectMapper() {
        return new ObjectMapper().registerModule(new CountryCollectorModule());
    }

    /**
     * Get a valid objectMapper-instance with the the correct modules registered,
     * and with a Map injected (using key {@link #INJECTED_MAP}) in its {@link InjectableValues},
     * containing a reference to this {@link PersistenceHandler}.
     * A new {@link ObjectMapper} with a "clean" map is generated each time,
     * ensuring that state is not shared between unrelated (de)serializations.
     * This Map can be used for sharing state downwards when (de)serializing objects.
     * Particularly useful for deserializing a tree of objects in need of sharing certain context,
     * e.g. which {@link World} the {@link globingular.core.Visit}s references.
     * 
     * @return an objectMapper instance
     */
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = getUninjectedObjectMapper();

        // Create a new Map and store a reference to this PersistenceHandler
        Map<String, Object> injectedMap = new HashMap<>();
        injectedMap.put(PersistenceHandler.INJECTED_MAP_PERSISTENCE, this);

        // Create a new InjectableValues-instance with the injectedMap and add to the mapper
        InjectableValues.Std injectableValues = new InjectableValues.Std();
        injectableValues.addValue(PersistenceHandler.INJECTED_MAP, injectedMap);
        mapper.setInjectableValues(injectableValues);

        return mapper;
    }

    /**
     * Return the predominant default world defined in the apps resources.
     * 
     * @return The default world-instance
     */
    public World getPredominantDefaultWorld() {
        return this.getDefaultWorld(predominantDefaultWorldName);
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
}
