package globingular.core;

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
     * @param countryCollector
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
        Country country = this.countryCollector.getVisitedCountries().stream()
        .sorted((c1, c2) -> Long.compare(c2.getPopulation(), c1.getPopulation())).findFirst().get();
        if (country == null) {
            return "-";
        }
        return country.getShortName() + " (" + country.getPopulation() + ")";
    }

}
