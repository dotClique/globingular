package globingular.core;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;

public class CountryCollector {

    /**
     * Set of visited countries, using countryCodes according to the ISO 3166-1
     * alpha-2 standard. Using SetProperty for observability and binding.
     */
    private final SetProperty<String> visits;

    /**
     * Defines a new CountryCollector-object without any visits.
     */
    public CountryCollector() {
        this.visits = new SimpleSetProperty<>(FXCollections.observableSet());
    }

    /**
     * Defines a new CountryCollector-object with the given countries set as
     * visited.
     * 
     * @param countries Array of countries that has been visited
     */
    public CountryCollector(final String... countries) {
        this();
        this.visits.set(FXCollections.observableSet(countries));
    }

    /**
     * Set visitation status of the given country.
     * 
     * @param countryCode The two-letter country code to set as visited. Using ISO
     *                    3166-1 alpha-2 codes
     */
    public void setVisited(final String countryCode) {
        this.visits.add(countryCode);
    }

    /**
     * Remove visitation status of the given country.
     * 
     * @param countryCode The two-letter country code to remove from visited
     *                    countries. Using ISO 3166-1 alpha-2 codes
     */
    public void removeVisited(final String countryCode) {
        this.visits.remove(countryCode);
    }

    /**
     * Check if the given country has been visited.
     * 
     * @param countryCode The two-letter country code to check. Using ISO 3166-1
     *                    alpha-2 codes
     * @return Returns true if the country has been visited. Returns false for all
     *         non-logged, including invalid names.
     */
    public boolean hasVisited(final String countryCode) {
        return this.visits.contains(countryCode);
    }

    /**
     * Get a list with all the countryCodes of the countries that has been visited.
     * 
     * @return List of countryCodes visited. Using ISO 3166-1 alpha-2 codes
     */
    public String[] getVisitedCountries() {
        return visits.toArray(new String[this.numberVisited()]);
    }

    /**
     * Get the amount of countries visited.
     * 
     * @return Returns number of countries visited
     */
    public int numberVisited() {
        return this.visits.size();
    }

    /**
     * Return a plain text representation of the object. Only used as a
     * human-readable representation. Not a stable interface.
     */
    @Override
    public String toString() {
        return this.visits.getValue().toString();
    }
}
