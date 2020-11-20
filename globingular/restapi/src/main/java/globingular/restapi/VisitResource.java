package globingular.restapi;

import java.io.IOException;

import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.persistence.FileHandler;
import globingular.persistence.PersistenceHandler;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * {@link Visit} resource, handling requests regarding visits.
 * See {@link CountryCollectorResource#getVisit()}
 */
public class VisitResource {

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
     * @param username           The username to store changes for
     * @param countryCollector   The {@link CountryCollector} to change
     * @param persistenceHandler The {@link PersistenceHandler} used to save app-state
     */
    public VisitResource(final String username, final CountryCollector countryCollector,
            final PersistenceHandler persistenceHandler) {
        this.username = username;
        this.countryCollector = countryCollector;
        this.persistenceHandler = persistenceHandler;
    }

    /**
     * Register a single Visit-instance for this {@link #countryCollector}.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * Using {@link POST} instead of {@link jakarta.ws.rs.PUT} because multiple
     * requests after each other may all perform actions on the server.
     * 
     * @param visit The visit to register
     * @return      True if successfully registered, otherwise false
     * 
     * @throws IOException If saving fails
     */
    @POST
    @Path("{register : (?i)register}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean registerVisit(final Visit visit) throws IOException {
        // Validate visit, register it, save app-state and return
        boolean result = this.countryCollector.registerVisit(validateAndReturnVisit(visit));
        saveAppState(username, countryCollector);
        return result;
    }

    /**
     * Remove a single {@link Visit}-instance from this {@link #countryCollector}.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * Using {@link POST} instead of {@link jakarta.ws.rs.DELETE},
     * as DELETE doesn't support sending message body required to remove the correct instance.
     * Using {@link POST} instead of {@link jakarta.ws.rs.PUT} because multiple
     * requests after each other may all perform actions on the server.
     * 
     * @param visit The visit to remove
     * @return      True if removed or non-existent
     * 
     * @throws IllegalArgumentException If username doesn't exist
     * @throws IOException              If saving fails
     */
    @POST
    @Path("{remove : (?i)remove}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeVisit(final Visit visit) throws IOException {
        // Validate visit, remove it, save app-state and return
        boolean result = this.countryCollector.removeVisit(validateAndReturnVisit(visit));
        saveAppState(username, countryCollector);
        return result;
    }

    /**
     * Make sure that the given Visit-instance is valid for this world,
     * and return a new Visit with the correct Country-instance instead.
     * 
     * @param visit The visit-instance to validate and convert
     * @return      The validated and converted visit-instance
     * 
     * @throws IllegalArgumentException If the given Visit-instance contains an invalid Country for this World
     */
    private Visit validateAndReturnVisit(final Visit visit) throws IllegalArgumentException {
        Visit newVisit = Visit.newVisitFromWorld(visit, this.countryCollector.getWorld());
        if (newVisit == null) {
            // If null is returned, it means the countryCode was invalid for this World
            throw new IllegalArgumentException("Unknown country " + visit.getCountry().getShortName()
                    + " for this World");
        }
        return newVisit;
    }

    /**
     * Saves the app-state for the active user, if {@link #persistenceHandler} is defined.
     * 
     * @param user         The username to save as
     * @param collector    The countryCollector to save
     * @return             True if successfully saved, or persistenceHandler is null.
     * 
     * @throws IOException If saving fails
     */
    private boolean saveAppState(final String user, final CountryCollector collector) throws IOException {
        if (this.persistenceHandler != null) {
            return FileHandler.saveCountryCollector(persistenceHandler, user, collector);
        }
        return true;
    }
}
