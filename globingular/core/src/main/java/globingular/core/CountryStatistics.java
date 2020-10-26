package globingular.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import globingular.core.CountryCollector;

import javafx.collections.ObservableSet;

public class CountryStatistics {

    public String getNrOfVisitedCountries(CountryCollector collector) {
        return Integer.toString(collector.numberVisited());
    }

    public String getMostPopulatedVisitedCountry(CountryCollector collector) {
        Map<String, Long> pop = new HashMap<String, Long>();
        ObservableSet<Country> visitedCountries = collector.getVisitedCountries();
        for (Country country : visitedCountries) {
            pop.put(country.getShortName(), country.getPopulation());
        }

        Entry<String, Long> maxEntry = Collections.max(pop.entrySet(), (Entry<String, Long> e1, 
        Entry<String, Long> e2) -> e1.getValue().compareTo(e2.getValue()));

        return maxEntry.getKey()+ "(" + Long.toString(maxEntry.getValue()) + ")";
    }

    public String getMostVisitedCountry() {
        return "";
    }

    public String getLongestDurationVisit() {
        return "";
    }

    public String getBiggestVisitedCountry() {
        return "";
    }
}