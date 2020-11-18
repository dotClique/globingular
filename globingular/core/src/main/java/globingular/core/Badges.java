package globingular.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Badges class creates goals and calculates progress towards them.
 */
public class Badges {

    /**
     * CountryCollector, manages which countries has been visited.
     */
    private final CountryCollector collector;

    /**
     * Constructor that initializes with a CountryCollector.
     *
     * @param collector - A CountryCollector that holds visited countries
     */
    public Badges(final CountryCollector collector) {
        this.collector = collector;
    }

    /**
     * Calculates the population of countries visited.
     *
     *
     * @return number between 0 and 1, representing a percentage.
     */
    public Double getCountriesVisitedBadge() {
        int visited = this.collector.numberOfCountriesVisited();
        int world = this.collector.getWorld().getCountries().size();

        return (double) visited / world;
    }


    /**
     * Calculates progress towards visiting a whole continent.
     *
     * @param region The continent
     * @return number between 0 and 1, representing a percentage.
     */
    public Double getContinentBadge(final String region) {
        long visited = this.collector.getVisitedCountries().stream().
                filter(c -> c.getRegion().equals(region)).count();
        long continent  = this.collector.getWorld().getCountries().stream().
                filter(c -> c.getRegion().equals(region)).count();

        return (double) visited / continent;
    }

    /**
     * Calculates the population of countries visited.
     *
     * @return number between 0 and 1, representing a percentage.
     */
    public Double getWorldPopulationBadge() {
        Set<Country> visitedCountries = this.collector.getVisitedCountries();
        long visitedPopulation = 0;
        for (Country country : visitedCountries) {
           visitedPopulation += country.getPopulation();
        }
        Set<Country> world = this.collector.getWorld().getCountries();
        long totalPopulation = 0;
        for (Country country : world) {
            totalPopulation += country.getPopulation();
        }

        return (double) visitedPopulation / totalPopulation;
    }

    /**
     * Map with all data.
     *
     * @return A map with strings.
     */
    public Map<String, String> getBadgeData() {
        Map<String, String> map = new HashMap<>();
        map.put("Europe", getContinentBadge("EU").toString());
        map.put("Asia", getContinentBadge("AS").toString());
        map.put("North-America", getContinentBadge("NA").toString());
        map.put("Africa", getContinentBadge("AF").toString());
        map.put("Antarctica", getContinentBadge("AN").toString());
        map.put("South-America", getContinentBadge("SA").toString());
        map.put("Oceania", getContinentBadge("OC").toString());
        map.put("World Population", getWorldPopulationBadge().toString());
        map.put("World", getCountriesVisitedBadge().toString());
        return map;
    }

}
