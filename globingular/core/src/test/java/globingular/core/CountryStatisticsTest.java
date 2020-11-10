package globingular.core;

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
    /**
     * Test for the method getNumberOfVisitedCountries() in CountryStatistics class.
     * @see globingular.core.CountryStatistics#getNumberOfVisitedCountries()
     */
    @Test
    public void testGetNumberOfVisitedCountries() {
        CountryStatistics countryStatistics = new CountryStatistics(collector);
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        assertEquals("0", countryStatistics.getNumberOfVisitedCountries());
        collector.registerVisit(country0);
        assertEquals("1", countryStatistics.getNumberOfVisitedCountries());
    }

    /**
     * Test for the method getMostPopulatedVisitedCountry() in CountryStatistics class.
     * @see globingular.core.CountryStatistics#getMostPopulatedVisitedCountry()
     */
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

    /**
     * Test to see if number of visited countries on a continent is correct.
     * @see globingular.core.CountryStatistics#getNumberOfCountriesVisitedOnContinent()
     */
    @Test
    public void testGetNumberOfCountriesVisitedOnContinent() {
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        CountryStatistics countryStatistics = new CountryStatistics(collector);
        assertEquals(0, countryStatistics.getNumberOfCountriesVisitedOnContinent("EU"));
        collector.registerVisit(country0);
        assertEquals(1, countryStatistics.getNumberOfCountriesVisitedOnContinent("EU"));
    }

    /**
     * Test to see if number of countries visited that start with letter is correct.
     * @see globingular.core.CountryStatistics#getNumberOfCountriesVisitedThatStartWithLetter()
     */
    @Test
    public void testGetNumberOfCountriesVisitedThatStartWithLetter() {
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        CountryStatistics countryStatistics = new CountryStatistics(collector);
        assertEquals(0, countryStatistics.getNumberOfCountriesVisitedThatStartWithLetter('A'));
        collector.registerVisit(country0);
        assertEquals(1, countryStatistics.getNumberOfCountriesVisitedThatStartWithLetter('A'));

    }
}