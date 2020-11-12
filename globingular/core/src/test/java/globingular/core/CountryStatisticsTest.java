package globingular.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class that tests the implementation of {@link CountryStatistics}.
 */
public class CountryStatisticsTest {
    static Country country0, country1;
    static World world0;
    static CountryCollector collector;

     @BeforeAll
    public static void start() {
        country0 = new Country("AD", "Andorra", "The Principality of Andorra", "UN", "EU", 77543);
        country1 = new Country("GF", "French Guiana", "Guyane", "France", "SA", 290601);
        world0 = new World(country0, country1);
        collector = new CountryCollector(world0);
    }

    @Test
    public void testGetNumberOfVisitedCountries() {
        CountryStatistics countryStatistics = new CountryStatistics(collector);
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        assertEquals("0", countryStatistics.getNumberOfVisitedCountries());
        collector.registerVisit(country0);
        assertEquals("1", countryStatistics.getNumberOfVisitedCountries());
    }

    @Test
    public void testGetMostPopulatedVisitedCountry() {
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        CountryStatistics countryStatistics = new CountryStatistics(collector);
        collector.registerVisit(country0);
        assertEquals("Andorra (77543)", countryStatistics.getMostPopulatedVisitedCountry());
        collector.registerVisit(country1);
        assertEquals("French Guiana (290601)", countryStatistics.getMostPopulatedVisitedCountry());
    }

    @Test
    public void testGetNumberOfCountriesVisitedInRegion() {
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        CountryStatistics countryStatistics = new CountryStatistics(collector);
        assertEquals(0, countryStatistics.getNumberOfCountriesVisitedInRegion("EU"));
        collector.registerVisit(country0);
        assertEquals(1, countryStatistics.getNumberOfCountriesVisitedInRegion("EU"));
    }

    @Test
    public void testGetNumberOfCountriesVisitedThatStartWithLetter() {
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        CountryStatistics countryStatistics = new CountryStatistics(collector);
        assertEquals(0, countryStatistics.getNumberOfCountriesVisitedThatStartWithLetter('A'));
        collector.registerVisit(country0);
        assertEquals(1, countryStatistics.getNumberOfCountriesVisitedThatStartWithLetter('A'));
        collector.registerVisit(country1);
        assertEquals(1, countryStatistics.getNumberOfCountriesVisitedThatStartWithLetter('A'));

    }
}