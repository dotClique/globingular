package globingular.restapi;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.core.Visit;
import globingular.persistence.PersistenceHandler;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * {@link CountryCollector} resource, handling requests for adding, changing
 * and deleting user data regarding CountryCollectors.
 * See {@link GlobingularService#getCountryCollector(String)}
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
     * @param globingularModule  The {@link GlobingularModule} to store changes in
     * @param username           The username to store changes for
     * @param countryCollector   The {@link CountryCollector} to change
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
     * Retrieve {@link CountryCollector} saved using the current {@link #username}.
     *
     * @return CountryCollector for the requested username.
     *         Returns null if there's no CountryCollector for this username
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CountryCollector getCountryCollector() {
        return this.countryCollector;
    }

    /**
     * Retrieve a {@link CountryCollector} from the request
     * and save using the current {@link #username}.
     * NB: Overwrites if there's already a value
     * 
     * @param collector The {@link CountryCollector} to save
     * @return          True if successfully saved
     * 
     * @throws IllegalArgumentException If CountryCollector is null
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putCountryCollector(final CountryCollector collector) throws IllegalArgumentException {
        if (collector == null) {
            throw new IllegalArgumentException("CountryCollector can't be null");
        }
        boolean result = this.globingularModule.putCountryCollector(username, collector);
        this.saveAppState(username, collector);
        return result;
    }

    /**
     * Delete a {@link CountryCollector} saved for the current {@link #username}.
     * 
     * @return True if successfully deleted, or if there was nothing saved using that username in the first place
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteCountryCollector() {
        // TODO: Go through all methods and reconsider the use of exceptions
        boolean result = this.globingularModule.removeCountryCollector(username);
        this.saveAppState(username, null);
        return result;
    }

    /**
     * Rename this {@link #username} from to the given {@code newName}.
     * After this operation the old username will be available,
     * and the {@link #countryCollector} can be retrieved using the new username.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * 
     * @param newName The new name to rename {@link #username} to
     * @return        True if successful, false if {@link #username} doesn't exist (no content to move)
     * 
     * @throws IllegalArgumentException If the new username is already taken
     */
    @POST
    @Path("{rename : (?i)rename}/{newName}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean renameCountryCollector(@QueryParam("newName") final String newName) throws IllegalArgumentException {
        if (this.globingularModule.isUsernameAvailable(username)) {
            // If there's no user with that username, return false
            return false;
        }
        if (!this.globingularModule.isUsernameAvailable(newName)) {
            // If newName is taken, throw exception
            throw new IllegalArgumentException("The new username is already taken: " + newName);
        }
        this.globingularModule.putCountryCollector(newName, countryCollector);
        this.globingularModule.removeCountryCollector(username);

        return false;
    }

    /**
     * Register a single Visit-instance for this {@link #countryCollector}.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * 
     * @param visit The visit to register
     * @return      True if successfully registered, otherwise false
     */
    @PUT
    @Path("{visit : (?i)visit}/{register : (?i)register}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean registerVisit(final Visit visit) {
        // We need to retrieve the correct Country-instance from this World
        Country country = this.countryCollector.getWorld().getCountryFromCode(visit.getCountry().getCountryCode());
        if (country == null) {
            // If a Country-instance is not found, it means the countryCode was invalid for this World
            throw new IllegalArgumentException("Unknown country " + visit.getCountry().getShortName()
                    + " for this World");
        }

        // Create a new Visit with the correct Country-instance
        Visit newVisit = new Visit(country, visit.getArrival(), visit.getDeparture());

        // Register visit, save app-state and return
        boolean result = this.countryCollector.registerVisit(newVisit);
        saveAppState(username, countryCollector);
        return result;
    }

    /**
     * Remove a single {@link Visit}-instance from this {@link #countryCollector}.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * Using a {@link PUT} instead of {@link jakarta.ws.rs.DELETE}
     * 
     * @param visit The visit to remove
     * @return      True if successfully removed, false otherwise
     * 
     * @throws IllegalArgumentException If username doesn't exist
     */
    @PUT
    @Path("visit/{remove : (?i)remove}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeVisit(final Visit visit) {
        // TODO: Change to countryCollector.removeVisit(visit)
        // Remove visit, save app-state and return
        for (Visit v : this.countryCollector.getVisits()) {
            if (v.equals(visit)) {
                boolean result = this.countryCollector.removeVisit(v);
                saveAppState(username, countryCollector);
                return result;
            }
        }
        return false;
    }

    /**
     * Saves the app-state for the active user, if {@link #persistenceHandler} is defined.
     * 
     * @param user         The username to save as
     * @param collector    The countryCollector to save
     */
    private void saveAppState(final String user, final CountryCollector collector) {
        if (this.persistenceHandler != null) {
            this.persistenceHandler.saveState(user, collector);
        }
    }
}
