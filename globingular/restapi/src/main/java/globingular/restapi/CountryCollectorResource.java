package globingular.restapi;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.persistence.PersistenceHandler;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path).
 */
public class CountryCollectorResource {

    /**
     * This instance's {@link GlobingularModule}.
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
     * The {@link PersistenceHandler} to use to save app-state.
     */
    private final PersistenceHandler persistenceHandler;

    /**
     * Initialize a CountryCollectorResource with context.
     * 
     * @param globingularModule The {@link GlobingularModule} to store changes in
     * @param username The username to store changes for
     * @param countryCollector The {@link CountryCollector} to change
     * @param persistenceHandler The {@link PersistenceHandler} used to save app-state
     */
    public CountryCollectorResource(final GlobingularModule globingularModule,
            final String username, final CountryCollector countryCollector,
            final PersistenceHandler persistenceHandler) {
        this.globingularModule = globingularModule;
        this.username = username;
        this.countryCollector = countryCollector;
        this.persistenceHandler = persistenceHandler;
    }

    /**
     * Retrieve a {@link CountryCollector} from the request
     * and save using the current {@link #username}.
     * NB: Overwrites if there's already a value
     * 
     * @param collector The {@link CountryCollector} to save
     * @return true if successfully saved, false otherwise
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putCountryCollector(final CountryCollector collector) {
        boolean result = this.globingularModule.putCountryCollector(username, collector);
        this.saveAppState(username, collector);
        return result;
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

    /**
     * Saves the app-state for the active user.
     * 
     * @param name a
     * @param collector a
     */
    private void saveAppState(final String name, final CountryCollector collector) {
        if (this.persistenceHandler != null) {
            this.persistenceHandler.saveState(name, collector);
        }
    }
}
