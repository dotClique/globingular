package globingular.ui;

import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.persistence.FileHandler;
import globingular.persistence.PersistenceHandler;

import java.io.IOException;

/**
 * Local file-based implementation of {@link GlobingularDataAccess}.
 */
public class LocalGlobingularDataAccess implements GlobingularDataAccess {
    /**
     * The provider of {@link com.fasterxml.jackson.databind.ObjectMapper}s and
     * injected values to use for (de)serialization.
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
     * @param persistenceHandler The provider of
     *                           {@link com.fasterxml.jackson.databind.ObjectMapper}s
     *                           and injected values to use for (de)serialization.
     */
    public LocalGlobingularDataAccess(final String username, final PersistenceHandler persistenceHandler) {
        this.username = username;
        this.persistenceHandler = persistenceHandler;
    }

    /**
     * {@inheritDoc} Loads from the local file system.
     */
    @Override
    public CountryCollector getCountryCollector() {
        final CountryCollector locallyStoredCountryCollector = FileHandler.loadCountryCollector(persistenceHandler,
                username);
        // If locallyStoredCountryCollector is null, the caller has to create a new one and save.
        return locallyStoredCountryCollector;
    }

    /**
     * {@inheritDoc} Saves to the local file system.
     */
    @Override
    public boolean saveCountryCollector(final CountryCollector collector) {
        try {
            return FileHandler.saveCountryCollector(persistenceHandler, username, collector);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc} Saves to the local file system.
     */
    public boolean renameCountryCollector(final String newUsername, final CountryCollector collector) {
        boolean saveResult = (new LocalGlobingularDataAccess(newUsername, persistenceHandler))
                .saveCountryCollector(collector);
        if (saveResult) {
            return this.deleteCountryCollector();
        }
        return false;
    }

    /**
     * {@inheritDoc} Deletes from the local file system.
     */
    @Override
    public boolean deleteCountryCollector() {
        try {
            return FileHandler.saveCountryCollector(persistenceHandler, username, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc} Saves to the local file system.
     */
    @Override
    public boolean saveVisit(final CountryCollector collector, final Visit visit) {
        return this.saveCountryCollector(collector);
    }

    /**
     * {@inheritDoc} Deletes from the local file system.
     */
    @Override
    public boolean deleteVisit(final CountryCollector collector, final Visit visit) {
        return this.saveCountryCollector(collector);
    }
}
