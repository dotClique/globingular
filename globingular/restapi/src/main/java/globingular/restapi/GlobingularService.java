package globingular.restapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import globingular.core.CountryCollector;
import globingular.persistence.FileHandler;
import globingular.persistence.PersistenceHandler;
import globingular.core.GlobingularModule;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * A service serving as the main handler for the API.
 * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
 */
@Path("{globingular : (?i)globingular}")
public class GlobingularService {

    /**
     * Logger-instance used to log in terminal.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GlobingularService.class);

    /**
     * The service's {@link GlobingularModule} instance, holding app-state.
     * Being injected from the Server setup implementing the API.
     */
    private final GlobingularModule globingularModule;

    /**
     * The service's {@link PersistenceHandler} instance, used for saving app-state.
     */
    private final PersistenceHandler persistenceHandler;

    /**
     * Construct a new GlobingularService using the given {@link GlobingularModule} as app-state.
     *
     * @param globingularModule App-state. Injected if not given.
     * @param persistenceHandler For saving app-state. Injected if not given.
     */
    @Inject
    public GlobingularService(final GlobingularModule globingularModule, final PersistenceHandler persistenceHandler) {
        this.globingularModule = globingularModule;
        this.persistenceHandler = persistenceHandler;
    }

    /**
     * Retrieve a {@link CountryCollectorResource} to handle all requests for the user.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * 
     * @param username The username to make changes for
     * @return         A {@link CountryCollectorResource}-instance to handle these requests
     */
    @Path("{countryCollector : (?i)countryCollector}/{username}")
    public CountryCollectorResource getCountryCollector(@PathParam("username") final String username) {
        LOG.info("->CountryCollectorResource({})", username);
        // Only allow lowercase usernames
        String usernameLowercase = username.toLowerCase();
        // Throw exception if username is invalid
        if (!GlobingularModule.isUsernameValid(usernameLowercase)) {
            throw new IllegalArgumentException("Username not valid: " + usernameLowercase);
        }

        // Get from cache if available
        CountryCollector countryCollector = this.globingularModule.getCountryCollector(usernameLowercase);
        // If not in cache, try to load from persistence
        if (countryCollector == null && persistenceHandler != null) {
            countryCollector = FileHandler.loadCountryCollector(persistenceHandler, usernameLowercase);
        }
        CountryCollectorResource resource = new CountryCollectorResource(this.globingularModule, usernameLowercase,
                countryCollector, this.persistenceHandler);
        LOG.debug("CountryCollectorResource for {} : {}", username, resource);
        return resource;
    }

    /**
     * Retrieve a {@link WorldResource} to handle requests regarding worlds.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * 
     * @return A {@link WorldResource}-instance to handle these requests
     */
    @Path("{world : (?i)world}")
    public WorldResource getWorld() {
        LOG.debug("->WorldResource()");
        // Return a WorldResource to handle requests
        WorldResource resource = new WorldResource(persistenceHandler);
        LOG.debug("WorldResource: {}", resource);
        return resource;
    }
}
