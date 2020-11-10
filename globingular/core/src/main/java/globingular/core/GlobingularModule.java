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
    private final Map<String, CountryCollector> map;

    /**
     * Initialize a new GlobingularModule with default parameters.
     */
    public GlobingularModule() {
        this.map = new HashMap<>();
    }

    /**
     * Retrieve a {@link CountryCollector} stored for the provided username.
     * Returns {@code null} if there is no CountryCollector stored for the provided username.
     * 
     * @param username The username to retrieve a countryCollector for
     * @return The countryCollector for the provided username. Returns {@code null} if no such countryCollector exists
     */
    public CountryCollector getCountryCollector(final String username) {
        return this.map.getOrDefault(username,
                new CountryCollector(new World(new Country("NO", "Norway"), new Country("JP", "Japan"))));
    }

    /**
     * Store a {@link CountryCollector} to a username.
     * 
     * @param username The username to save the countryCollector as
     * @param countryCollector The countryCollector to save
     * @param overwrite Whether or not to overwrite (update/replace) an already
     * present countryCollector for the provided username
     * @return true if it was saved, false if not
     */
    public boolean putCountryCollector(final String username,
            final CountryCollector countryCollector, final boolean overwrite) {
        if (overwrite || this.isUsernameAvailable(username)) {
            this.map.put(username, countryCollector);
            return true;
        }
        return false;
    }

    /**
     * Check if the provided username is available.
     * 
     * @param username The username to check if is available
     * @return True if this username is not in use
     */
    public boolean isUsernameAvailable(final String username) {
        return this.map.get(username) == null;
    }
}
