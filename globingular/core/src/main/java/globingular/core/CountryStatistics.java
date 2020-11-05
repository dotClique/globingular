package globingular.core;

import java.util.Comparator;

/**
 * The CountryStatistics class creates statistics that can be viewed in UI.
 */
public class CountryStatistics {

    /**
     * CountryCollector that holds the data needed for statistics.
     */
    private final CountryCollector countryCollector;

    /**
     * Constructor that initializes with a CountryCollector.
     * 
     * @param countryCollector that holds visited countries
     */
    public CountryStatistics(final CountryCollector countryCollector) {
        this.countryCollector = countryCollector;
    }
    /**
     * Gets the number of visited countries from a countryCollector.
     * 
     * @return string representing the number of visited countries
     */
    public String getNumberOfVisitedCountries() {
        return Integer.toString(this.countryCollector.numberVisited());
    }

    /**
     * Gets the most populated country visited.
     * 
     * @return a country name and the population as a string
     */
    public String getMostPopulatedVisitedCountry() {
        Country country = this.countryCollector
                .getVisitedCountries()
                .stream()
                .max(Comparator.comparingLong(Country::getPopulation))
                .orElse(null);

        if (country == null) {
            return "-";
        }
        return country.getShortName() + " (" + country.getPopulation() + ")";
    }
}
