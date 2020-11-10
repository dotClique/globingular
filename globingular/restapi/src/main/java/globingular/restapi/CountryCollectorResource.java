package globingular.restapi;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.core.Visit;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path).
 */
public class CountryCollectorResource {

    /**
     * This instance's {@link GlobingularModule}, not currently in use. TODO:
     */
    private final GlobingularModule globingularModule;
    /**
     * The username this instance is for.
     */
    private final String username;
    /**
     * The {@link CountryCollector} this instance works on.
     */
    private final CountryCollector countryCollector;

    /**
     * Initialize a CountryCollectorResource with context.
     * 
     * @param globingularModule The {@link GlobingularModuel} to store changes in
     * @param username The username to store changes for
     * @param countryCollector The {@link CountryCollector} to change
     */
    public CountryCollectorResource(final GlobingularModule globingularModule,
            final String username, final CountryCollector countryCollector) {
        this.globingularModule = globingularModule;
        this.username = username;
        this.countryCollector = countryCollector;
    }

    /**
     * Retrieve a {@link CountryCollector} from the request
     * and save using the current {@link #username}.
     * NB: Overwrites if there's already a value
     * 
     * @param collector The {@link CountryCollector} to save
     * @return Returns true if save, false otherwise
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putCountryCollector(final CountryCollector collector) {
        return this.globingularModule.putCountryCollector(username, collector, true);
    }

    /**
     * Register a new {@link Visit} for the current {@link #countryCollector}.
     * 
     * @param visit The {@link Visit} to register.
     */
    @POST
    @Path("/visit")
    @Consumes(MediaType.APPLICATION_JSON)
    public void postVisit(final Visit visit) {
        this.countryCollector.registerVisit(visit);
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CountryCollector getCountryCollector() {
        return this.countryCollector;
    }
}
