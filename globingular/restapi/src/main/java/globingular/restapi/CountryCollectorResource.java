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
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * CountryCollector resource. See {@link GlobingularService#getCountryCollector(String)}
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
     * Retrieve {@link CountryCollector} saved using the current {@link #username}.
     *
     * @return CountryCollector for the requested username
     * 
     * @throws IllegalArgumentException if username doesn't exist
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CountryCollector getCountryCollector() throws IllegalArgumentException {
        checkUsernameExists();
        return this.countryCollector;
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
     * @return true if successfully deleted
     * 
     * @throws IllegalArgumentException if username doesn't exist
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteCountryCollector() throws IllegalArgumentException {
        checkUsernameExists();
        return this.globingularModule.removeCountryCollector(username);
    }

    /**
     * Rename this {@link #username} from to the given {@code newName}.
     * Effectively it stores a new reference to the {@link #countryCollector}
     * with the new username {@code newName} and then deletes the old reference.
     * 
     * @param newName the new name to rename {@link #username} to
     * @return        true if successful, false otherwise
     * 
     * @throws IllegalArgumentException if username doesn't exist
     *                                  or if the new username is already taken
     */
    @POST
    @Path("rename/{newName}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean renameCountryCollector(@PathParam("newName") final String newName) throws IllegalArgumentException {
        checkUsernameExists();
        if (!this.globingularModule.isUsernameAvailable(newName)) {
            throw new IllegalArgumentException("The new username is already taken: " + newName);
        }
        this.globingularModule.putCountryCollector(newName, countryCollector);
        this.globingularModule.removeCountryCollector(username);

        return this.globingularModule.getCountryCollector(newName) == countryCollector
                && this.globingularModule.isUsernameAvailable(username);
    }

    /**
     * Register a single Visit-instance for this {@link #countryCollector}.
     * 
     * @param visit the visit to register
     * @return      true if successfully registered, otherwise false
     */
    @PUT
    @Path("visit/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean registerVisit(final Visit visit) {
        checkUsernameExists();
        // We need to retrieve the correct Country-instance from this World
        Country country = this.countryCollector.getWorld().getCountryFromCode(visit.getCountry().getCountryCode());
        if (country == null) {
            // If a Country-instance is not found, it means the countryCode was invalid for this World
            throw new IllegalArgumentException("Unknown country " + visit.getCountry().getShortName()
                    + " for this World");
        }

        // Create a new Visit with the correct Country-instance
        Visit newVisit = new Visit(country, visit.getArrival(), visit.getDeparture());
        this.countryCollector.registerVisit(newVisit);
        return this.countryCollector.getVisits().contains(newVisit);
    }

    /**
     * Remove a single {@link Visit}-instance from this {@link #countryCollector}.
     * 
     * @param visit the visit to remove
     * @return true if successfully removed, false otherwise
     * 
     * @throws IllegalArgumentException if username doesn't exist
     */
    @PUT
    @Path("visit/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeVisit(final Visit visit) throws IllegalArgumentException {
        checkUsernameExists();
        for (Visit e : this.countryCollector.getVisits()) {
            if (e.equals(visit)) {
                this.countryCollector.removeVisit(e);
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the current {@link #username} exists.
     * Throws {@link IllegalArgumentException} if nonexistent.
     * 
     * @throws IllegalArgumentException if username doesn't exist
     */
    private void checkUsernameExists() throws IllegalArgumentException {
        if (this.globingularModule.isUsernameAvailable(username)) {
            throw new IllegalArgumentException("The given username doesn't exist: " + username);
        }
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
