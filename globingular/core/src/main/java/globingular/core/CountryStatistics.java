package globingular.core;

import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

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
            .filter(c -> c.getRegion().equals(continent.toUpperCase()))
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
        Map<String, String> map = new HashMap<String, String>();
        map.put("Number of visited countries", String.valueOf(getNumberOfVisitedCountries()));
        map.put("Most populated visited country", getMostPopulatedVisitedCountry());
        map.put("Number of countries visited in Europe", String.valueOf(getNumberOfCountriesVisitedOnContinent("EU")));
        map.put("Number of countries visited in Asia", String.valueOf(getNumberOfCountriesVisitedOnContinent("AS")));
        map.put("Number of countries visited in Africa", String.valueOf(getNumberOfCountriesVisitedOnContinent("AF")));
        map.put("Number of countries visited in North America",
            String.valueOf(getNumberOfCountriesVisitedOnContinent("NA")));
        map.put("Number of countries visited in South America",
            String.valueOf(getNumberOfCountriesVisitedOnContinent("SA")));
        map.put("Number of countries visited in Oceania",
            String.valueOf(getNumberOfCountriesVisitedOnContinent("OC")));
        map.put("Number of countries that start with letter A",
            String.valueOf(getNumberOfCountriesVisitedThatStartWithLetter('A')));

        return map;
    }
}
