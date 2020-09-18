package globingular.core;

import java.util.List;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;

public class CountryCollector {

    final private SetProperty<String> visits;

    /**
     * Defines a new CountryCollector-object without any visits
     */
    public CountryCollector() {
        this.visits = new SimpleSetProperty<>(FXCollections.observableSet());
    }

    /**
     * Defines a new CountryCollector-object with the given countries set as visited
     * 
     * @param countries Array of countries that has been visited
     */
    public CountryCollector(String... countries) {
        this();
        this.visits.set(FXCollections.observableSet(countries));
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
     * Get a list with all the countryCodes of the countries that has been visited
     * 
     * @return List of countryCodes visited
     */
    public String[] getVisitedCountries() {
        return visits.toArray(new String[this.numberVisited()]);
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
        return this.visits.getValue().toString();
    }
}
