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
        this.registerVisit(new Visit(country, arrival, departure));
    }

    /**
     * Register a country-visit.
     * 
     * @param visit The visit object to register
     * 
     * @throws IllegalArgumentException If the given visited country does not exist in this instance's world
     */
    public void registerVisit(final Visit visit) {
        // Check to make sure the Visit includes a valid Country
        throwExceptionIfInvalidCountry(visit.getCountry());
        // Add the given Visit to visits
        this.visits.add(visit);
        // Keep visitedCountries up to date
        this.visitedCountries.add(visit.getCountry());
        // Notify listeners about addition
        this.notifyListeners(new ChangeEvent<>(ChangeEvent.Status.ADDED, visit));
    }

    /**
     * Remove all visits to the given country from the log.
     * 
     * @param country The country to remove visits for
     * 
     * @throws IllegalArgumentException If the given country does not exist in this instance's world
     */
    public void removeAllVisitsToCountry(final Country country) throws IllegalArgumentException {
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
    }

    /**
     * Remove the given visit from the log.
     * 
     * @param visit The visit-object to remove
     */
    public void removeVisit(final Visit visit) {
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
     * 
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
     * Get listeners for changes in visited countries.
     */
    @Override
    public Collection<Listener<Visit>> getListeners() {
        // Return a new unmodifiable set containing the listeners.
        return Set.copyOf(listeners);
    }
    /**
     * {@inheritDoc}
     * Allows for listening to changes in visited countries.
     */
    @Override
    public void addListener(final Listener<Visit> listener) {
        // Return true if already registered, or if successfully added.
        this.listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     * The given listener will no longer be notified about changes in visited countries.
     */
    @Override
    public void removeListener(final Listener<Visit> listener) {
        // Return true if listener successfully removed, or isn't registered in the first place.
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
