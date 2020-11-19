package globingular.core;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class that tests {@link Badges}.
 */
public class BadgesTest {
    static CountryCollector collector;
    static Country country0, country1, country3;
    static World world0;

    @BeforeAll
    public static void start() {
        country0 = new Country("GS",
                "South Georgia and the South Sandwich Islands",
                "South Georgia and the South Sandwich Islands", "United Kingdom",
                "AN",30 );
        country1 = new Country("AQ","Antarctica",
                "All land and ice shelves south of the 60th parallel south", "Antarctic Treaty",
                "AN", 0);
        country3 = new Country("AE", "United Arab Emirates", "The United Arab Emirates",
                "UN", "AS", 9890400);
        world0 = new World(country0, country1, country3);
        collector = new CountryCollector(world0);

    }

    @Test
    public void testGetVisitedCountriesBadge() {
        Badges badges = new Badges(collector);
        collector.registerVisit(country0);
        collector.registerVisit(country1);
        collector.removeAllVisitsToCountry(country3);
        assertEquals((double)2/3,badges.getCountriesVisitedBadge(),0.001);
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        assertEquals(0.0, badges.getCountriesVisitedBadge(), 0.00001);
    }

    @Test
    public void testGetContinentBadge() {
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        Badges badges = new Badges(collector);
        collector.registerVisit(country0);
        assertEquals(0.5, badges.getContinentBadge("AN"), 0.001);
        collector.registerVisit(country1);
        assertEquals(1.0, badges.getContinentBadge("AN"), 0.001);
    }

    @Test
    public void testGetWorldPopulationBadge() {
        collector.removeAllVisitsToCountry(country0);
        collector.removeAllVisitsToCountry(country1);
        Badges badges = new Badges(collector);
        assertEquals(0, badges.getWorldPopulationBadge(), 0.00001);
        collector.registerVisit(country3);
        assertEquals((double)9890400/9890430,badges.getWorldPopulationBadge(), 0.00001);
    }


}
