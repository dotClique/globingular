package globingular.restapi;

import java.io.IOException;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
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
     * @throws IOException              If saving fails
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putCountryCollector(final CountryCollector collector) throws IllegalArgumentException, IOException {
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
     * 
     * @throws IOException If saving fails
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteCountryCollector() throws IOException {
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
     * @throws IOException              If saving fails
     */
    @POST
    @Path("{rename : (?i)rename}/{newName}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean renameCountryCollector(@QueryParam("newName") final String newName) throws IllegalArgumentException,
            IOException {
        if (this.globingularModule.isUsernameAvailable(username)) {
            // If there's no user with that username, return false
            return false;
        }
        if (!this.globingularModule.isUsernameAvailable(newName)) {
            // If newName is taken, throw exception
            throw new IllegalArgumentException("The new username is already taken: " + newName);
        }

        boolean resultPut = this.globingularModule.putCountryCollector(newName, countryCollector);
        boolean resultRemove = this.globingularModule.removeCountryCollector(username);

        this.saveAppState(username, null);
        this.saveAppState(newName, countryCollector);

        return resultPut && resultRemove;
    }

    /**
     * Retrieve a {@link VisitResource} to handle requests regarding visits.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     * 
     * @return A {@link VisitResource}-instance to handle these requests
     * 
     * @throws IllegalArgumentException If username doesn't exist
     */
    @Path("{visit : (?i)visit}")
    public VisitResource getVisit() throws IllegalArgumentException {
        // If username doesn't exist, throw exception
        if (this.globingularModule.isUsernameAvailable(username)) {
            throw new IllegalArgumentException("Username doesn't exist: " + username);
        }
        // Return a VisitResource to handle requests
        return new VisitResource(username, countryCollector, persistenceHandler);
    }

    /**
     * Saves the app-state for the active user, if {@link #persistenceHandler} is defined.
     * 
     * @param user         The username to save as
     * @param collector    The countryCollector to save
     * 
     * @throws IOException If saving fails
     */
    private void saveAppState(final String user, final CountryCollector collector) throws IOException {
        if (this.persistenceHandler != null) {
            this.persistenceHandler.saveState(user, collector);
        }
    }
}
