package globingular.restapi;

import globingular.core.CountryCollector;
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
     * Construct a new GlobingularService using the given {@link GlobingularModule} as app-state.
     *
     * @param globingularModule App-state. Injected if not given.
     */
    @Inject
    public GlobingularService(final GlobingularModule globingularModule) {
        this.globingularModule = globingularModule;
    }

    /**
     * Retrieve the {@link GlobingularModule} being used.
     * 
     * @return The {@link GlobingularModule}-instance
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GlobingularModule getGlobingularModule() {
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
        CountryCollector countryCollector = getGlobingularModule().getCountryCollector(username);
        return new CountryCollectorResource(globingularModule, username, countryCollector);
    }
}
