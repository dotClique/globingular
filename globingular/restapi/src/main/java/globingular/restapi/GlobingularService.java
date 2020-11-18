package globingular.restapi;

import globingular.core.CountryCollector;
import globingular.persistence.PersistenceHandler;
import globingular.core.GlobingularModule;
import globingular.core.World;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * A service serving as the main handler for the API.
 * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
 */
@Path("{globingular : (?i)globingular}")
public class GlobingularService {

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
            countryCollector = this.persistenceHandler.loadCountryCollector(usernameLowercase);
        }
        return new CountryCollectorResource(this.globingularModule, usernameLowercase, countryCollector,
                this.persistenceHandler);
    }

    /**
     * Retrieve a default {@link World} from {@link #persistenceHandler}.
     * Returns null if {@link #persistenceHandler} is null or if the
     * worldName isn't defined as a default world.
     * 
     * @param worldName the worldName to retrieve
     * @return          the requested World if it exists, otherwise null
     */
    @GET
    @Path("{worldName}")
    @Produces(MediaType.APPLICATION_JSON)
    public World getWorld(@PathParam("worldName") final String worldName) {
        if (persistenceHandler != null) {
            return persistenceHandler.getDefaultWorld(worldName);
        }
        return null;
    }
}
