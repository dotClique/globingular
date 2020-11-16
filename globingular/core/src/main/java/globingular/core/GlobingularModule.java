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
        return this.countryCollectorsByUsername.get(username.toLowerCase());
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
        this.countryCollectorsByUsername.put(username.toLowerCase(), countryCollector);
        return true; // TODO: should this method just be void?
    }

    /**
     * Check if the provided username is available.
     * 
     * @param username The username to check if is available
     * @return True if this username is not in use
     */
    public boolean isUsernameAvailable(final String username) {
        return this.countryCollectorsByUsername.containsKey(username.toLowerCase());
    }

    /**
     * Check if the provided username is valid.
     * 
     * @param username The username to check validity of
     * @return True if this username is valid (alphanumeric)
     */
    public boolean isUsernameValid(final String username) {
        return username.matches("[A-Za-z0-9]+");
    }
}
