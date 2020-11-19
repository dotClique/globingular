package globingular.persistence;

import globingular.core.CountryCollector;
import globingular.core.GlobingularDataAccess;

/**
 * Local file-based implementation of {@link GlobingularDataAccess}.
 */
public class LocalGlobingularDataAccess implements GlobingularDataAccess {
    /**
     * The provider of {@link com.fasterxml.jackson.databind.ObjectMapper}s
     * and injected values to use for (de)serialization.
     */
    private final PersistenceHandler persistenceHandler;
    /**
     * The username whose storage to use.
     */
    private final String username;

    /**
     * Construct a new instance which will use the given arguments for persistence.
     *
     * @param username           The username whose storage to use.
     * @param persistenceHandler The provider of {@link com.fasterxml.jackson.databind.ObjectMapper}s
     *                           and injected values to use for (de)serialization.
     */
    public LocalGlobingularDataAccess(final String username, final PersistenceHandler persistenceHandler) {
        this.username = username;
        this.persistenceHandler = persistenceHandler;
    }

    /**
     * {@inheritDoc}
     * Loads from the local file system.
     *
     */
    @Override
    public CountryCollector getCountryCollector() {
        return FileHandler.loadCountryCollector(persistenceHandler, username);
    }

    /**
     * {@inheritDoc}
     * Saves to the local file system.
     */
    @Override
    public boolean putCountryCollector(final CountryCollector collector) {
        return FileHandler.saveCountryCollector(persistenceHandler, username, collector);
    }
}
