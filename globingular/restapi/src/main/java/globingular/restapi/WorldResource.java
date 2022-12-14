package globingular.restapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import globingular.core.World;
import globingular.persistence.PersistenceHandler;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * {@link World} resource, handling requests regarding Worlds.
 * See {@link GlobingularService#getWorld()}
 */
public class WorldResource {

    /**
     * Logger-instance used to log in terminal.
     */
    private static final Logger LOG = LoggerFactory.getLogger(WorldResource.class);

    /**
     * The {@link PersistenceHandler} used to store World data.
     */
    private final PersistenceHandler persistenceHandler;

    /**
     * Initialize a WorldResource with context.
     * 
     * @param persistenceHandler The {@link PersistenceHandler} used to store World data
     */
    public WorldResource(final PersistenceHandler persistenceHandler) {
        this.persistenceHandler = persistenceHandler;
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
        LOG.debug("getWorld({})", worldName);
        try {
            if (persistenceHandler != null) {
                return persistenceHandler.getDefaultWorld(worldName);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
