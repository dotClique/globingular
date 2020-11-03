package globingular.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javafx.collections.ObservableSet;

/**
 * The CountryStatistics class creates statistics that can be viewed in UI.
 */
public class CountryStatistics {

    /**
     * CountryCollector that holds the data needed for statistics.
     */
    private final CountryCollector collector;

    /**
     * Constructor that initializes with a CountryCollector
     */
    public CountryStatistics(final CountryCollector collector) {
        this.collector = collector;

    }
    /**
     * Gets the number of visited countries from a countryCollector.
     * 
     * @param collector
     * @return string representing the number of visited countries
     */
    public String getNumberOfVisitedCountries(final CountryCollector collector) {
        return Integer.toString(this.collector.numberVisited());
    }

    /**
     * Gets the most populated country visited.
     * 
     * @param collector
     * @return a country name and the population as a string
     */
    public String getMostPopulatedVisitedCountry(final CountryCollector collector) {
        Map<String, Long> pop = new HashMap<String, Long>();
        ObservableSet<Country> visitedCountries = this.collector.getVisitedCountries();
        for (Country country : visitedCountries) {
            pop.put(country.getShortName(), country.getPopulation());
        }

        Entry<String, Long> maxEntry = Collections.max(pop.entrySet(), (Entry<String, Long> e1,
        Entry<String, Long> e2) -> e1.getValue().compareTo(e2.getValue()));

        return maxEntry.getKey() + " (" + Long.toString(maxEntry.getValue()) + ")";
    }

}
