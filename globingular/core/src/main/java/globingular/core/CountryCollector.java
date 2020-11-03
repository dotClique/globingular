package globingular.core;

import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.transformation.SortedList;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>CountryCollector class aggregates visits to various countries.</p>
 * 
 * <p>An instance of the CountryCollector class contains:
 * <ul>
 * <li>Methods for getting, setting and removing a country's visited status</li>
 * <li>Getting visited countries</li>
 * <li>Getting number of visited countries</li>
 * </ul>
 * </p>
 */

public class CountryCollector {

    /**
     * Main set, containing all country-visits.
     */
    private final SetProperty<Visit> visits = new SimpleSetProperty<>(FXCollections.observableSet());

    /**
     * Synchronized read-only set of visits.
     * Using ReadOnlySetWrapper::getReadOnlyProperty to prevent reassignment,
     * FXCollections::unmodifiableObservableSet to prevent modification by users.
     */
    private final ReadOnlySetProperty<Visit> visitsReadOnly =
            new ReadOnlySetWrapper<>(FXCollections.unmodifiableObservableSet(visits)).getReadOnlyProperty();

    /**
     * Set containing only the countries visited. Listens to {@link #visits}.
     * Don't pass directly to users, rather use visitedCountriesReadOnly.
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

        // Register a SetChangeListener to keep visits and visitedCountries in-sync
        visits.addListener((SetChangeListener<? super Visit>) e -> {
            if (e.wasAdded()) {
                visitedCountries.add(e.getElementAdded().getCountry());
            } else {
                Country country = e.getElementRemoved().getCountry();
                if (visits.stream().noneMatch(v -> v.getCountry() == country)) {
                    visitedCountries.remove(country);
                }
            }
        });
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
     * Register a country-visit. Assumes the arrival and departure was now as none is given.
     * 
     * @param country The Country to register visit to
     * 
     * @throws IllegalArgumentException If Country does not exist in this instance's world
     * @throws IllegalArgumentException If Country has already been visited
     */
    public void registerVisit(final Country country) {
        this.registerVisit(country, null, null);
    }

    /**
     * Register a country-visit.
     * 
     * @param country The Country to register visit to
     * @param arrival The time of arrival
     * @param departure The time of departure
     * 
     * @throws IllegalArgumentException If Country does not exist in this instance's world
     * @throws IllegalArgumentException If Country has already been visited
     */
    public void registerVisit(final Country country, final LocalDateTime arrival, final LocalDateTime departure) {
        final Visit visit = new Visit(country, arrival, departure);
        this.registerVisit(visit);
    }

    /**
     * Register a country-visit.
     * 
     * @param visit The visit object to register
     * 
     * @throws IllegalArgumentException If the given visited country does not exist in this instance's world
     */
    public void registerVisit(final Visit visit) {
        if (!world.countryExists(visit.getCountry())) {
            throw new IllegalArgumentException("Unknown country "
                + visit.getCountry().getShortName() + " for this World");
        }
        this.visits.add(visit);
    }

    /**
     * Remove all visits to the given country from the log.
     * 
     * @param country The country to remove visits for
     * 
     * @throws IllegalArgumentException If the given country does not exist in this instance's world
     */
    public void removeAllVisitsToCountry(final Country country) {
        if (!world.countryExists(country)) {
            throw new IllegalArgumentException("Unknown country " + country.getShortName() + " for this World");
        }
        List<Visit> arr = this.visits.stream().filter(v -> v.getCountry() == country).collect(Collectors.toList());
        this.visits.removeAll(arr);
    }

    /**
     * Remove the given visit from the log.
     * 
     * @param visit The visit-object to remove
     * 
     * @throws IllegalArgumentException If the given visited country does not exist in this instance's world
     */
    public void removeVisit(final Visit visit) {
        if (!world.countryExists(visit.getCountry())) {
            throw new IllegalArgumentException("Unknown country "
                + visit.getCountry().getShortName() + " for this World");
        }
        this.visits.remove(visit);
    }

    /**
     * Retrieve all registered visits to the given country.
     * 
     * @param country The country to retrieve Visits for
     * @return A collection containing every visit to the given country
     */
    public Collection<Visit> getCountryVisits(final Country country) {
        return this.visits.stream().filter(v -> v.getCountry() == country).collect(Collectors.toList());
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
     * Get a readonly synchronized set-property containing the visits logged.
     * Synchronized with this instance's main set.
     *
     * @return Readonly synchronized set-property of all visits
     */
    public ReadOnlySetProperty<Visit> visitsProperty() {
        return visitsReadOnly;
    }

    /**
     * Get a collection containing all visits to the given Country.
     * NB: Visits are immutable, so there's no leak here.
     * 
     * @param country The country to retrieve visits to
     * @return A collection containing all (current) Visits to the given country
     */
    public Collection<Visit> getVisitsToCountry(final Country country) {
        return this.getVisits().stream().filter(v -> v.getCountry() == country).collect(Collectors.toList());
    }

    /**
     * Get a readonly observable synchronized set containing the visited countries.
     * Synchronized with this instances's main set.
     *
     * @return Readonly observable set containing the visited countries
     */
    public ObservableSet<Visit> getVisits() {
        return visitsProperty().get();
    }

    /**
     * Get a readonly synchronized set-property containing the visited countries.
     * Synchronized indirectly with this instance's main set.
     *
     * @return Readonly synchronized set-property of all visits
     */
    public ReadOnlySetProperty<Country> visitedCountriesProperty() {
        return visitedCountriesReadOnly;
    }

    /**
     * Get a readonly observable synchronized set containing the visited countries.
     * Synchronized indirectly with this instances's main set.
     *
     * @return Readonly observable set containing the visited countries
     */
    public ObservableSet<Country> getVisitedCountries() {
        return visitedCountriesProperty().get();
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
        return this.visits.getValue().toString();
    }
}
