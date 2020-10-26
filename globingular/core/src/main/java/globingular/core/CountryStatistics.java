package globingular.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javafx.collections.ObservableSet;

/**
 * The CountryStatistics class creates statistics that can be used in the AppController.
 */
public class CountryStatistics {

    /**
     * Gets the number of visited countries from a countrycollector.
     * 
     * @param collector
     * @return string representing the number of visited countries
     */
    public String getNrOfVisitedCountries(final CountryCollector collector) {
        return Integer.toString(collector.numberVisited());
    }

    /**
     * Gets the most populated country visited.
     * 
     * @param collector
     * @return a country name and the population as a string
     */
    public String getMostPopulatedVisitedCountry(final CountryCollector collector) {
        Map<String, Long> pop = new HashMap<String, Long>();
        ObservableSet<Country> visitedCountries = collector.getVisitedCountries();
        for (Country country : visitedCountries) {
            pop.put(country.getShortName(), country.getPopulation());
        }

        Entry<String, Long> maxEntry = Collections.max(pop.entrySet(), (Entry<String, Long> e1,
        Entry<String, Long> e2) -> e1.getValue().compareTo(e2.getValue()));

        return maxEntry.getKey() + "(" + Long.toString(maxEntry.getValue()) + ")";
    }

    //TODO implement methods below
    //public String getMostVisitedCountry() {
    //    return "";
    //}

    //public String getLongestDurationVisit() {
    //    return "";
    //}

    //public String getBiggestVisitedCountry() {
    //    return "";
    //}
}
