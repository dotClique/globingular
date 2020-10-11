package globingular.core;

import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.SortedList;

import java.util.Comparator;

public class CountryCollector {

    /**
     * Main set of visited countries. Don't pass directly to users, rather use visitedCountriesReadOnly.
     */
    private final SetProperty<Country> visitedCountries = new SimpleSetProperty<>(FXCollections.observableSet());
    /**
     * Synchronized read-only set of visited countries.
     * Using ReadOnlySetWrapper::getReadOnlyProperty to prevent reassignment,
     * FXCollections::unmodifiableObservableSet to prevent modification by users.
     */
    private final ReadOnlySetProperty<Country> visitedCountriesReadOnly =
            new ReadOnlySetWrapper<>(FXCollections.unmodifiableObservableSet(visitedCountries)).getReadOnlyProperty();
    /**
     * Get a readonly sorted list-view of this instance's set of visited countries.
     */
    private final SortedList<Country> visitedCountriesSorted =
            CustomBindings.createSortedListView(this.visitedCountries,
                                                Comparator.comparing(Country::getShortName));
    /**
     * The World this instance collects countries from.
     */
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
     * @throws IllegalArgumentException If Country does not exist in this instances's world
     */
    public void setVisited(final Country country) {
        if (!world.countryExists(country)) {
            throw new IllegalArgumentException("Unknown country " + country.getShortName() + " for this World");
        }
        this.visitedCountries.add(country);
    }

    /**
     * Remove visitation status of the given country.
     *
     * @param country The Country to mark as not visited
     * @throws IllegalArgumentException If Country does not exist in this instances's world
     */
    public void removeVisited(final Country country) {
        if (!world.countryExists(country)) {
            throw new IllegalArgumentException("Unknown country " + country.getShortName() + " for this World");
        }
        this.visitedCountries.remove(country);
    }

    /**
     * Check if the given country has been visited.
     *
     * @param country The Country to check
     * @return Returns true if the country has been visited
     * @throws IllegalArgumentException If Country does not exist in this instances's world
     */
    public boolean isVisited(final Country country) {
        if (!world.countryExists(country)) {
            throw new IllegalArgumentException("Unknown country " + country.getShortName() + " for this World");
        }
        return this.visitedCountries.contains(country);
    }

    /**
     * Get a readonly synchronized set-property containing the visited countries.
     * Synchronized with this instance's main set.
     *
     * @return Readonly synchronized set-property of all visits
     */
    public ReadOnlySetProperty<Country> visitedCountriesProperty() {
        return visitedCountriesReadOnly;
    }

    /**
     * Get a readonly observable synchronized set containing the visited countries.
     * Synchronized with this instances's main set.
     *
     * @return Readonly observable set containing the visited countries
     */
    public ObservableSet<Country> getVisitedCountries() {
        return visitedCountriesReadOnly.get();
    }

    /**
     * Get a readonly synchronized sorted-list view of the visitedCountries-property.
     * Synchronized with this instances's main set.
     *
     * @return Readonly synchronized sorted-list view of the visitedCountries-property
     */
    public SortedList<Country> getVisitedCountriesSorted() {
        return visitedCountriesSorted;
    }

    /**
     * Get the number of countries visited.
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
