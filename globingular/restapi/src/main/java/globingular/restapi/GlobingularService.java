package globingular.restapi;

import globingular.core.CountryCollector;
import globingular.persistence.PersistenceHandler;
import globingular.core.GlobingularModule;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * A service serving as the main handler for the API.
 */
@Path("/globingular")
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
     * Retrieve the {@link GlobingularModule} being used.
     * 
     * @return The {@link GlobingularModule}-instance
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GlobingularModule getGlobingularModule() {
        // TODO: Need to implement a GlobingularModuleSerializer (and Deserializer), or remove this method.
        // TODO: Exposing globingularModule to edits?
        return this.globingularModule;
    }

    /**
     * Retrieve a {@link CountryCollectorResource} to handle all requests for the user.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * 
     * @param username The username to make changes for
     * @return A {@link CountryCollectorResource}-instance to handle these requests
     */
    @Path("/{countryCollector : (?i)countryCollector}/{username}")
    public CountryCollectorResource getCountryCollector(@PathParam("username") final String username) {
        // Get from cache if available
        CountryCollector countryCollector = getGlobingularModule().getCountryCollector(username);
        // If not in cache, load from persistence
        if (countryCollector == null) {
            countryCollector = this.persistenceHandler.loadCountryCollector(username);
        }
        return new CountryCollectorResource(this.globingularModule, username, countryCollector,
                this.persistenceHandler);
    }
}
