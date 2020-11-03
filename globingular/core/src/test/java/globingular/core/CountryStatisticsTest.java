package globingular.core;

import globingular.core.World;
import globingular.core.CountryCollector;
import globingular.core.CountryStatistics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class that tests the implementation of country statistics.
 */
public class CountryStatisticsTest {
    static Country country0, country1;
    static World world0;
    static CountryCollector collector;

     @BeforeAll
    public static void start() {
        country0 = new Country("AD", "Andorra", "The Principality of Andorra", "UN", "EU", 77543, new Province[0]);
        country1 = new Country("GF", "French Guiana", "Guyane", "France", "SA", 290601, new Province[0]);
        world0 = new World(country0, country1);
        collector = new CountryCollector(world0);
    }
    
    @Test
    public void testGetNumberOfVisitedCountries() {
        CountryStatistics countryStatistics = new CountryStatistics();
        collector.removeVisited(country0);
        collector.removeVisited(country1);
        assertEquals("0", countryStatistics.getNumberOfVisitedCountries(collector));
        collector.setVisited(country0);
        assertEquals("1", countryStatistics.getNumberOfVisitedCountries(collector));
    }

    @Test
    public void testGetMostPopulatedVisitedCountry() {
        collector.removeVisited(country0);
        collector.removeVisited(country1);
        CountryStatistics cs = new CountryStatistics();
        collector.setVisited(country0);
        assertEquals("Andorra (77543)", countryStatistics.getMostPopulatedVisitedCountry(collector));
        collector.setVisited(country1);
        assertEquals("French Guiana (290601)", countryStatistics.getMostPopulatedVisitedCountry(collector));
    }
}