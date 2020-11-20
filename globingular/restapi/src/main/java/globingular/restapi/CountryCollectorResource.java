package globingular.restapi;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.persistence.FileHandler;
import globingular.persistence.PersistenceHandler;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * {@link CountryCollector} resource, handling requests for adding, changing
 * and deleting user data regarding CountryCollectors.
 * See {@link GlobingularService#getCountryCollector(String)}
 */
public class CountryCollectorResource {

    /**
     * Logger-instance used to log in terminal.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CountryCollectorResource.class);

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
        this.username = username.toLowerCase();
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
        LOG.debug("getCountryCollector({})", username);
        try {
            return this.countryCollector;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieve a {@link CountryCollector} from the request
     * and save using the current {@link #username}.
     * NB: Overwrites if there's already a value
     *
     * @param newCountryCollector The {@link CountryCollector} to save at this username.
     * @return                    True if successfully saved, false otherwise
     *
     * @throws WebApplicationException If CountryCollector is null
     * @throws IOException             If saving fails
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putCountryCollector(final CountryCollector newCountryCollector)
            throws WebApplicationException, IOException {
        LOG.debug("putCountryCollector({}, {})", username, newCountryCollector);
        try {
            if (newCountryCollector == null) {
                throw new WebApplicationException("CountryCollector can't be null", Response.Status.BAD_REQUEST);
            }
            boolean result = this.globingularModule.putCountryCollector(username, newCountryCollector);
            this.saveCountryCollector(username, newCountryCollector);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
        LOG.debug("deleteContryCollecto({})", username);
        try {
            boolean result = this.globingularModule.removeCountryCollector(username);
            this.saveCountryCollector(username, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
     * @throws WebApplicationException If the new username is already taken
     * @throws IOException             If saving fails
     */
    @POST
    @Path("{rename : (?i)rename}/{newName}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean renameCountryCollector(@PathParam("newName") final String newName)
            throws WebApplicationException, IOException {
        LOG.debug("renameCountryCollector({}, {})", username, newName);
        try {
            if (newName == null) {
                // If newName is null, throw exception
                throw new WebApplicationException("New username can't be null", Response.Status.BAD_REQUEST);
            }
            final String newNameLowercase = newName.toLowerCase();

            if (this.globingularModule.isUsernameAvailable(username)) {
                // If there's no user with that username, return false
                return false;
            }
            if (!this.globingularModule.isUsernameAvailable(newNameLowercase)) {
                // If newName is taken, throw exception
                throw new WebApplicationException("The new username is already taken: " + newNameLowercase,
                        Response.Status.BAD_REQUEST);
            }

            boolean resultPut = this.globingularModule.putCountryCollector(newNameLowercase, countryCollector);
            boolean resultRemove = this.globingularModule.removeCountryCollector(username);

            this.saveCountryCollector(username, null);
            this.saveCountryCollector(newNameLowercase, countryCollector);

            return resultPut && resultRemove;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieve a {@link VisitResource} to handle requests regarding visits.
     * Using {@code : (?i)} in {@code @Path} to enable case-insensitivity.
     *
     * @return A {@link VisitResource}-instance to handle these requests
     *
     * @throws WebApplicationException If username doesn't exist
     */
    @Path("{visit : (?i)visit}")
    public VisitResource getVisit() throws WebApplicationException {
        LOG.debug("->VisitResource({})", username);
        try {
            // If username doesn't exist, throw exception
            if (this.globingularModule.isUsernameAvailable(username)) {
                throw new WebApplicationException("Username doesn't exist: " + username, Response.Status.BAD_REQUEST);
            }
            // Return a VisitResource to handle requests
            VisitResource resource = new VisitResource(username, countryCollector, persistenceHandler);
            LOG.debug("VisitResouce for {} : {}", username, resource);
            return resource;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Helper method to save the given CountryCollector at the given username,
     * if {@link #persistenceHandler} is defined.
     *
     * @param usernameToSaveAt The username to save at.
     * @param countryCollectorToSave The CountryCollector to save.
     *
     * @throws IOException If saving fails
     */
    private void saveCountryCollector(final String usernameToSaveAt, final CountryCollector countryCollectorToSave)
            throws IOException {
        if (this.persistenceHandler != null) {
            FileHandler.saveCountryCollector(persistenceHandler, usernameToSaveAt, countryCollectorToSave);
        }
    }
}
