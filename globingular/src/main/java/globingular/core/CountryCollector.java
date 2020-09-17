package globingular.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CountryCollector {

    private Set<String> visits;

    /**
     * Defines a new CountryCollector-object without any visits
     */
    public CountryCollector() {
        this.visits = new HashSet<>();
    }

    /**
     * Defines a new CountryCollector-object with the given countries set as visited
     * 
     * @param countries Array of countries that has been visited
     */
    public CountryCollector(String... countries) {
        this();
        this.visits.addAll(List.of(countries));
    }

    /**
     * Set visitation status of the given country
     * 
     * @param countryCode The two-letter country code to log
     */
    public void setVisited(String countryCode) {
        this.visits.add(countryCode);
    }

    /**
     * Remove visitation status of the given country
     * 
     * @param countryCode The two-letter country code to log
     */
    public void removeVisited(String countryCode) {
        this.visits.remove(countryCode);
    }

    /**
     * Check if the given country has been visited
     * 
     * @param countryCode The two-letter country code to check
     * @return Returns true if the country has been visited. Returns false for all
     *         non-logged, including invalid names.
     */
    public boolean hasVisited(String countryCode) {
        return this.visits.contains(countryCode);
    }

    /**
     * Get the amount of countries visited
     * 
     * @return Returns number of countries visited
     */
    public int numberVisited() {
        return this.visits.size();
    }

    @Override
    public String toString() {
        return this.visits.toString();
    }
}
