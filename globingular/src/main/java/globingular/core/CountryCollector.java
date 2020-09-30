package globingular.core;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public class CountryCollector {

    /**
     * Set of visited countries. Using SetProperty for observability and binding.
     */
    private final SetProperty<Country> visitedCountries = new SimpleSetProperty<>(FXCollections.observableSet());
    private final ObservableList<Country> visitedCountriesSorted =
            CustomBindings.createSortedListView(this.visitedCountries);
    private final World world;

    /**
     * Create a new CountryCollector using the provided World as its source of existing countries.
     *
     * @param world Source of existing countries
     */
    public CountryCollector(final World world) {
        this.world = world;
    }

    /**
     * Get the World providing this CountryCollector's list of existing coutries.
     *
     * @return The World providing this CountryCollector's list of existing coutries
     */
    public World getWorld() {
        return world;
    }

    /**
     * Set visitation status of the given country.
     *
     * @param country The Country to log
     */
    public void setVisited(final Country country) {
        if (!world.countryExists(country)) {
            throw new IllegalArgumentException("Unknown country " + country.getName() + " for this World");
        }
        this.visitedCountries.add(country);
    }

    /**
     * Remove visitation status of the given country.
     *
     * @param country The Country to mark as not visited
     */
    public void removeVisited(final Country country) {
        if (!world.countryExists(country)) {
            throw new IllegalArgumentException("Unknown country " + country.getName() + " for this World");
        }
        this.visitedCountries.remove(country);
    }

    /**
     * Check if the given country has been visited.
     *
     * @param country The Country to check
     * @return Returns true if the country has been visited. Returns false for all
     * non-logged countries
     */
    public boolean hasVisited(final Country country) {
        if (!world.countryExists(country)) {
            throw new IllegalArgumentException("Unknown country " + country.getName() + " for this World");
        }
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
     * Get the property responsible for keeping track of visited countries
     *
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

    /**
     * Return a plain text representation of the object. Only used as a
     * human-readable representation. Not a stable interface.
     */
    @Override
    public String toString() {
        return this.visitedCountries.getValue().toString();
    }
}
