package globingular.core;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

/**
 * The CountryStatistics class creates statistics about visited countries.
 */
public class CountryStatistics {

    /**
     * CountryCollector that holds the data needed for statistics.
     */
    private final CountryCollector countryCollector;

    /**
     * Locale-specific formatter for statistic numbers.
     */
    private final NumberFormat statisticFormat = NumberFormat.getInstance();

    /**
     * Constructor that initializes with a CountryCollector.
     * 
     * @param countryCollector - A CountryCollector that holds visited countries
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
        return Integer.toString(this.countryCollector.numberOfCountriesVisited());
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
        return country.getShortName() + " (" + statisticFormat.format(country.getPopulation()) + ")";
    }

    /**
     * Gets the number of visited countries in a region.
     * 
     * @param region a string representation of the region
     * @return the number of countries visited on that region
     */
    public Long getNumberOfCountriesVisitedInRegion(final String region) {
        return this.countryCollector
            .getVisitedCountries()
            .stream()
            .filter(c -> c.getRegion().equals(region.toUpperCase()))
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

    /**
     * Map with all the statistics.
     * 
     * @return map of strings
     */
    public Map<String, String> getAllStatistics() {
        Map<String, String> map = new HashMap<>();
        map.put("Number of visited countries", getNumberOfVisitedCountries());
        map.put("Most populated visited country", getMostPopulatedVisitedCountry());
        map.put("Number of countries visited in Europe", getNumberOfCountriesVisitedInRegion("EU").toString());
        map.put("Number of countries visited in Asia", getNumberOfCountriesVisitedInRegion("AS").toString());
        map.put("Number of countries visited in Africa", getNumberOfCountriesVisitedInRegion("AF").toString());
        map.put("Number of countries visited in North America",
            getNumberOfCountriesVisitedInRegion("NA").toString());
        map.put("Number of countries visited in South America",
            getNumberOfCountriesVisitedInRegion("SA").toString());
        map.put("Number of countries visited in Oceania",
            getNumberOfCountriesVisitedInRegion("OC").toString());
        map.put("Number of countries that start with letter A",
            getNumberOfCountriesVisitedThatStartWithLetter('A').toString());

        return map;
    }
}
