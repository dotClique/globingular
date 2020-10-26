package globingular.core;

import java.time.LocalDateTime;

/**
 * <p>Visit class contains the necessary metadata about a specific visit to a country.</p>
 * 
 * <p>An instance of the Visit class contains:
 * <ul>
 * <li>Methods for retrieving info about which country has been visited and when</li>
 * </ul>
 * </p>
 */
public class Visit {

    /**
     * The country this visit was to.
     */
    private final Country country;

    /**
     * The time of arrival in the country.
     */
    private final LocalDateTime arrival;

    /**
     * Initiate a new visit to the given country at the given time.
     * 
     * @param country The country that has been visited
     * @param arrival The arrival time to log in the visit
     */
    public Visit(final Country country, final LocalDateTime arrival) {
        this.country = country;
        this.arrival = arrival;
    }

    /**
     * Retrieve the country this visit-object details.
     * 
     * @return The country that has been visited
     */
    public Country getCountry() {
        return this.country;
    }

    /**
     * Retrieve the date and time of arrival to the country.
     * 
     * @return A LocalDateTime of the arrival in the country
     */
    public LocalDateTime getArrival() {
        return this.arrival;
    }

    /**
     * Retrieve a String representation of the Visit object.
     * This is not a stable interface, and is only to be used for human readable representation.
     * 
     * @return A simplified string representation of the Visit
     */
    @Override
    public String toString() {
        return String.format("[%s at %s]", this.country.getShortName(), this.arrival);
    }
}
