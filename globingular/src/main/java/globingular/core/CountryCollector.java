package globingular.core;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.*;

import static java.util.Arrays.binarySearch;

public class CountryCollector {

    private final SetProperty<String> visitedCountries;
    private final ObservableList<String> visitedCountriesSorted;

    /**
     * Defines a new CountryCollector-object with the given countries set as visited
     *
     * @param countries Array of countries that has been visited
     */
    public CountryCollector(String... countries) {
        this.visitedCountries = new SimpleSetProperty<>(FXCollections.observableSet(countries));
        this.visitedCountriesSorted = CustomBindings.createSortedListView(visitedCountries);
    }

    /**
     * Set visitation status of the given country
     *
     * @param countryCode The two-letter country code to log
     */
    public void setVisited(String countryCode) {
        this.visitedCountries.add(countryCode);
    }

    /**
     * Remove visitation status of the given country
     *
     * @param countryCode The two-letter country code to log
     */
    public void removeVisited(String countryCode) {
        this.visitedCountries.remove(countryCode);
    }

    /**
     * Check if the given country has been visited
     *
     * @param countryCode The two-letter country code to check
     * @return Returns true if the country has been visited. Returns false for all
     * non-logged, including invalid names.
     */
    public boolean hasVisited(String countryCode) {
        return this.visitedCountries.contains(countryCode);
    }

    /**
     * Get an observable set of all visits
     *
     * @return Observable set of all visits
     */
    public ObservableSet<String> getVisitedCountries() {
        return visitedCountries.get();
    }

    /**
     * Get the property responsible for keeping track of visited countries
     * @return Property responsible for keeping track of visited countries
     */
    public SetProperty<String> visitedCountriesProperty() {
        return visitedCountries;
    }

    /**
     * Get a sorted-list view of the visitedCountries-property
     *
     * @return Sorted-list view of the visitedCountries-property
     */
    public final ObservableList<String> getVisitedCountriesSorted() {
        return visitedCountriesSorted;
    }

    /**
     * Get the amount of countries visited
     *
     * @return Returns number of countries visited
     */
    public int numberVisited() {
        return this.visitedCountries.size();
    }

    @Override
    public String toString() {
        return this.visitedCountries.getValue().toString();
    }
}
