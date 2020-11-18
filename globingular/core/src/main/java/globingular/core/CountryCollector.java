package globingular.core;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>CountryCollector class aggregates visits to various countries.</p>
 * 
 * <p>An instance of the CountryCollector class contains:
 * <ul>
 * <li>Methods for getting, setting and removing visits to a country</li>
 * <li>Getting visits and visited countries</li>
 * <li>Getting number of visits and visited countries</li>
 * </ul>
 * </p>
 */

public class CountryCollector implements Observable<Visit> {

    /**
     * Main set, containing all country-visits.
     */
    private final Set<Visit> visits = new HashSet<>();
    /**
     * Set containing only the countries visited.
     * Kept up to date with {@link #visits}.
     */
    private final Set<Country> visitedCountries = new HashSet<>();
    /**
     * The World this instance collects countries from.
     */
    private final World world;
    /**
     * Collection holding all added listeners.
     */
    private final Collection<Listener<Visit>> listeners;

    /**
     * Create a new CountryCollector using the provided World as its source of existing countries.
     *
     * @param world Source of existing countries
     */
    public CountryCollector(final World world) {
        this.world = world;
        this.listeners = new HashSet<>();
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
     * @return        True, if the visit was added
     * 
     * @throws IllegalArgumentException If Country does not exist in this instance's world
     * @throws IllegalArgumentException If Country has already been visited
     */
    public boolean registerVisit(final Country country) {
        return this.registerVisit(country, null, null);
    }

    /**
     * Register a country-visit.
     * 
     * @param country   The Country to register visit to
     * @param arrival   The time of arrival
     * @param departure The time of departure
     * @return          True, if the visit was added
     * 
     * @throws IllegalArgumentException If Country does not exist in this instance's world
     * @throws IllegalArgumentException If Country has already been visited
     */
    public boolean registerVisit(final Country country, final LocalDateTime arrival, final LocalDateTime departure) {
        return this.registerVisit(new Visit(country, arrival, departure));
    }

    /**
     * Register a country-visit.
     * 
     * @param visit The visit object to register
     * @return      True, if the visit was added
     * 
     * @throws IllegalArgumentException If the given visited country does not exist in this instance's world
     */
    public boolean registerVisit(final Visit visit) {
        // Check to make sure the Visit includes a valid Country
        throwExceptionIfInvalidCountry(visit.getCountry());
        // Add the given Visit to visits
        this.visits.add(visit);
        // Keep visitedCountries up to date
        this.visitedCountries.add(visit.getCountry());
        // Notify listeners about addition
        this.notifyListeners(new ChangeEvent<>(ChangeEvent.Status.ADDED, visit));
        // Returns true, as the element has been added
        return true;
    }

    /**
     * Remove all visits to the given country from the log.
     * 
     * @param country The country to remove visits for
     * @return        True, if the visit was removed
     * 
     * @throws IllegalArgumentException If the given country does not exist in this instance's world
     */
    public boolean removeAllVisitsToCountry(final Country country) throws IllegalArgumentException {
        // Check to make sure the given Country is valid
        throwExceptionIfInvalidCountry(country);
        // Retrieve all visits to the given Country
        Iterator<Visit> iterator = this.getVisitsToCountry(country).iterator();
        // Loop through all visits
        while (iterator.hasNext()) {
            // Retrieve visit
            Visit visit = iterator.next();
            // Remove retrieved visit
            this.visits.remove(visit);
            // Remove country before notifying listeners, if no more visits to remove
            if (!iterator.hasNext()) {
                // Keep visitedCountries up to date
                this.visitedCountries.remove(country);
            }
            // Notify listeners about removal of visit
            this.notifyListeners(new ChangeEvent<>(ChangeEvent.Status.REMOVED, visit));
        }
        // Return true, as the element was removed
        return true;
    }

    /**
     * Remove the given visit from the log.
     * 
     * @param visit The visit-object to remove
     * @return      True, if the visit was removed
     * 
     * @throws IllegalArgumentException If the given country does not exist in this instance's world
     */
    public boolean removeVisit(final Visit visit) throws IllegalArgumentException {
        // Check to make sure the given Visit contains a valid Country
        throwExceptionIfInvalidCountry(visit.getCountry());
        // Remove the given Visit from visits
        this.visits.remove(visit);
        // Keep visitedCountries up to date
        if (this.visits.stream().noneMatch(v -> v.getCountry() == visit.getCountry())) {
            this.visitedCountries.remove(visit.getCountry());
        }
        // Notify listeners about removal
        this.notifyListeners(new ChangeEvent<>(ChangeEvent.Status.REMOVED, visit));
        // Return true, as the element was removed
        return true;
    }

    /**
     * Check if the given country has been visited.
     *
     * @param country The Country to check
     * @return Returns true if the country has been visited
     * 
     * @throws IllegalArgumentException If Country does not exist in this instances's world
     */
    public boolean isVisited(final Country country) throws IllegalArgumentException {
        // Check to make sure the given Country is valid
        throwExceptionIfInvalidCountry(country);
        // Retrieve and return
        return this.visitedCountries.contains(country);
    }

    /**
     * Get a collection containing all visits to the given {@link Country}.
     * NB: Visits are immutable, so there's no leak here.
     * 
     * @param country The country to retrieve visits to
     * @return A collection containing all (current) Visits to the given country
     */
    public Collection<Visit> getVisitsToCountry(final Country country) {
        // Check to make sure the given Country is valid
        throwExceptionIfInvalidCountry(country);
        // Return visits to the given Country
        return this.visits.stream().filter(v -> v.getCountry() == country).collect(Collectors.toList());
    }

    /**
     * Get an unmodifiable set containing the visited countries.
     *
     * @return Unmodifiable set containing the visited countries
     */
    public Set<Visit> getVisits() {
        return Collections.unmodifiableSet(this.visits);
    }

    /**
     * Get an unmodifiable set containing the visited countries.
     *
     * @return Unmodifiable set containing the visited countries
     */
    public Set<Country> getVisitedCountries() {
        return Collections.unmodifiableSet(this.visitedCountries);
    }

    /**
     * Get the number of visits.
     * 
     * @return Return number of visits logged
     */
    public int numberOfVisits() {
        return this.visits.size();
    }

    /**
     * Get the number of countries visited.
     *
     * @return Returns number of countries visited
     */
    public int numberOfCountriesVisited() {
        return this.visitedCountries.size();
    }

    /**
     * Throw exception if the given {@link Country} is invalid for this {@link CountryCollector}.
     * The country is considered valid if it's contained in this countryCollector's {@link #world}.
     * 
     * @param country The given country to check if valid
     * 
     * @throws IllegalArgumentException If the given country is invalid
     */
    private void throwExceptionIfInvalidCountry(final Country country) throws IllegalArgumentException {
        if (!world.countryExists(country)) {
            throw new IllegalArgumentException("Unknown country " + country.getShortName() + " for this World");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.visits.toString();
    }

    /**
     * {@inheritDoc}
     * Allows for listening to changes in visited countries.
     */
    @Override
    public void addListener(final Listener<Visit> listener) {
        this.listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     * The given listener will no longer be notified about changes in visited countries.
     */
    @Override
    public void removeListener(final Listener<Visit> listener) {
        this.listeners.remove(listener);
    }

    /**
     * Notify all registered listeners.
     * 
     * @param event A ChangeEvent to pass to listeners
     */
    private void notifyListeners(final ChangeEvent<Visit> event) {
        // Notify all listeners
        this.listeners.forEach(l -> l.notifyListener(event));
    }
}
