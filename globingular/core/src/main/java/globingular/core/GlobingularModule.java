package globingular.core;

import java.util.HashMap;
import java.util.Map;

/**
 * A GlobingularModule contains appstates saved on a per user basis, using only usernames without any authentication.
 */
public class GlobingularModule {
    /**
     * The main map of the class, holding the username and appstates ({@link CountryCollector}).
     */
    private final Map<String, CountryCollector> countryCollectorsByUsername;

    /**
     * Initialize a new GlobingularModule with default parameters.
     */
    public GlobingularModule() {
        this.countryCollectorsByUsername = new HashMap<>();
    }

    /**
     * Retrieve a {@link CountryCollector} stored for the provided username.
     * Returns {@code null} if there is no CountryCollector stored for the provided username.
     * 
     * @param username The username to retrieve a countryCollector for
     * @return The countryCollector for the provided username. Returns {@code null} if no such countryCollector exists
     */
    public CountryCollector getCountryCollector(final String username) {
        return this.countryCollectorsByUsername.get(username);
    }

    /**
     * Store a {@link CountryCollector} to a username.
     * 
     * @param username The username to save the countryCollector as
     * @param countryCollector The countryCollector to save
     * present countryCollector for the provided username
     * @return true if successfully saved, false otherwise
     */
    public boolean putCountryCollector(final String username,
            final CountryCollector countryCollector) {
        this.countryCollectorsByUsername.put(username, countryCollector);
        return !isUsernameAvailable(username);
    }

    /**
     * Remove {@link CountryCollector} stored with given username.
     * 
     * @param username The username to remove CountryCollector for
     * @return true if successfully removed, false otherwise
     */
    public boolean removeCountryCollector(final String username) {
        this.countryCollectorsByUsername.remove(username);
        return isUsernameAvailable(username);
    }

    /**
     * Check if the provided username is available.
     * 
     * @param username The username to check if is available
     * @return True if this username is not in use
     */
    public boolean isUsernameAvailable(final String username) {
        return !this.countryCollectorsByUsername.containsKey(username);
    }
}
