package globingular.core;

import globingular.json.PersistenceHandler;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;

public class CountryCollector {

    /**
     * Responsible for persisting state across runs.
     */
    private final PersistenceHandler persistence;

    /**
     * Set of visited countries, using countryCodes according to the ISO 3166-1
     * alpha-2 standard. Using SetProperty for observability and binding.
     */
    private final SetProperty<String> visits;

    /**
     * Defines a new CountryCollector-object with given persistence-handler.
     * 
     * @param persistence The PersistenceHandler to use for this instance.
     */
    public CountryCollector(final PersistenceHandler persistence) {
        this.visits = new SimpleSetProperty<>(FXCollections.observableSet());
        this.persistence = persistence;
    }

    /**
     * Defines a new CountryCollector-object without any visits and with a new PersistenceHandler.
     */
    public CountryCollector() {
        this(new PersistenceHandler());
    }

    /**
     * Defines a new CountryCollector-object with the given countries set as
     * visited and with a new PersistenceHandler.
     * 
     * @param countries Array of countries that has been visited
     */
    public CountryCollector(final String... countries) {
        this();
        this.visits.set(FXCollections.observableSet(countries));
    }

    /**
     * Defines a new CountryCollector-object with the given countries set as
     * visited and using the given PersistenceHandler.
     * 
     * @param persistence The PersistenceHandler to use
     * @param countries Array of countries that has been visited
     */
    public CountryCollector(final PersistenceHandler persistence, final String... countries) {
        this(persistence);
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
        this.persistence.saveState(this);
    }

    /**
     * Remove visitation status of the given country.
     * 
     * @param countryCode The two-letter country code to remove from visited
     *                    countries. Using ISO 3166-1 alpha-2 codes
     */
    public void removeVisited(final String countryCode) {
        this.visits.remove(countryCode);
        this.persistence.saveState(this);
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
     * Load CountryCollector-state from file.
     * 
     * @return Returns a new countryCollector-object from file
     */
    public static CountryCollector loadState() {
        PersistenceHandler persistence = new PersistenceHandler();
        CountryCollector cc = persistence.loadState();
        return cc;
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
