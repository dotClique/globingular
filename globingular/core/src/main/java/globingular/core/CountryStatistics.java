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

    /**
     * Gets the number of visited countries on each continent.
     * 
     * @param continent a string representation of the continent
     * @return the number of countries visited on that continent
     */
    public Long getNumberOfCountriesVisitedOnContinent(final String continent) {
        return this.countryCollector
            .getVisitedCountries()
            .stream()
            .filter(c -> c.getRegion().equals(continent))
            .count();
    }

    /**
     * Gets countries that start with a specific letter.
     * 
     * @param letter a character
     * @return the number of countries visited that start with this letter
     */
    public Long getNumberOfCountriesVisitedThatStartWithLetter(final char letter) {
        return this.countryCollector
            .getVisitedCountries()
            .stream()
            .filter(c -> c.getShortName().startsWith(String.valueOf(letter).toUpperCase()))
            .count();
    }
}
