package globingular.core;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.*;

import java.util.*;

import static java.util.Arrays.binarySearch;

public class CountryCollector {

    private final SetProperty<Country> visitedCountries;
    private final ObservableList<Country> visitedCountriesSorted;

    /**
     * Defines a new CountryCollector-object with the given countries set as visited
     *
     * @param countries Array of countries that has been visited
     */
    public CountryCollector(Country... countries) {
        this.visitedCountries = new SimpleSetProperty<>(FXCollections.observableSet(countries));
        this.visitedCountriesSorted = CustomBindings.createSortedListView(visitedCountries);
    }

    /**
     * Set visitation status of the given country
     *
     * @param country The Country to log
     */
    public void setVisited(Country country) {
        this.visitedCountries.add(country);
    }

    /**
     * Remove visitation status of the given country
     *
     * @param country The Country to log
     */
    public void removeVisited(Country country) {
        this.visitedCountries.remove(country);
    }

    /**
     * Check if the given country has been visited
     *
     * @param country The Country to check
     * @return Returns true if the country has been visited. Returns false for all
     * non-logged countries
     */
    public boolean hasVisited(Country country) {
        return this.visitedCountries.contains(country);
    }

    /**
     * Get an observable set of all visits.
     * Cannot be iterated safely while removing items.
     *
     * @return Observable set of all visits
     */
    public ObservableSet<Country> getVisitedCountries() {
        return visitedCountries.get();
    }

    /**
     * Get a copy of the set of visited countries
     * @return A new set containing the same elements as the visitedCountries-set
     */
    public Set<Country> getVisitedCountriesCopy() {
        return new HashSet<>(Arrays.asList(getVisitedCountries().toArray(Country[]::new)));
    }

    /**
     * Get the property responsible for keeping track of visited countries
     * @return Property responsible for keeping track of visited countries
     */
    public SetProperty<Country> visitedCountriesProperty() {
        return visitedCountries;
    }

    /**
     * Get a sorted-list view of the visitedCountries-property
     *
     * @return Sorted-list view of the visitedCountries-property
     */
    public final ObservableList<Country> getVisitedCountriesSorted() {
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
