package globingular.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CountryCollectorTest {

    @BeforeAll
    public static void start() {

    }

    @Test
    public void testToString() {
        CountryCollector cc = new CountryCollector();
        CountryCollector.Country country1 = cc.new Country("C1", "Country1", "Kingdom of Country1", "UN",
                                                           "EU", 5_000_000L, new Province[]{});
        CountryCollector.Country country2 = cc.new Country("C2", "Country2", "Kingdom of Country2", "EU",
                                                           10_000_000L);
        CountryCollector.Country country3 = cc.new Country("C3", "Country3", "Kingdom of Country3", "UN",
                                                           47_431_256L);
        cc.setVisited(country2);
        assertEquals(
                "[Country{countryCode='C2', name='Country2', longname='Kingdom of Country2', sovereignty='UN', " +
                "region='EU', population=10000000, provinces=[]}]",
                cc.toString());
    }

    @Test
    public void testGetCountryFromCountryCode() {
        CountryCollector cc = new CountryCollector();
        assertEquals(cc.new Country("C5", "Country5", "ca", "fs", 0L), cc.getCountryFromCode("C5"));
    }

    @Test
    public void testGetCountryFromCountryName() {
        CountryCollector cc = new CountryCollector();
        assertEquals(cc.new Country("C6", "Country6", "cd", "lo", 0L), cc.getCountryFromName("Country6"));
    }

    @Test
    public void testConstructor() {
        CountryCollector cc = new CountryCollector();
        CountryCollector.Country country1 = cc.new Country("C1", "Country1", "Kingdom of Country1", "UN",
                                                           "EU", 5_000_000L, new Province[]{});
        CountryCollector.Country country2 = cc.new Country("C2", "Country2", "Kingdom of Country2", "EU",
                                                           10_000_000L);
        CountryCollector.Country country3 = cc.new Country("C3", "Country3", "Kingdom of Country3", "UN",
                                                           47_431_256L);
        cc.setVisited(country1);
        cc.setVisited(country2);
        assertEquals(2, cc.numberVisited());
        assertTrue(cc.hasVisited(country2));
        assertTrue(cc.hasVisited(country1));
        assertFalse(cc.hasVisited(country3));
    }

    @Test
    public void testEmptyConstructor() {
        CountryCollector cc = new CountryCollector();
        assertEquals(0, cc.numberVisited());
        assertEquals("[]", cc.toString());
    }

    @Test
    public void testSetVisited() {
        CountryCollector cc = new CountryCollector();
        CountryCollector.Country country3 = cc.new Country("C3", "Country3", "Kingdom of Country3", "UN",
                                                           47_431_256L);
        assertFalse(cc.hasVisited(country3));
        cc.setVisited(country3);
        assertTrue(cc.hasVisited(country3));
    }

    @Test
    public void testRemoveVisited() {
        CountryCollector cc = new CountryCollector();
        CountryCollector.Country country2 = cc.new Country("C2", "Country2", "Kingdom of Country2", "EU",
                                                           10_000_000L);
        cc.setVisited(country2);
        assertTrue(cc.hasVisited(country2));
        cc.removeVisited(country2);
        assertFalse(cc.hasVisited(country2));
    }

    @Test
    public void testHasVisited() {
        CountryCollector cc = new CountryCollector();
        CountryCollector.Country country2 = cc.new Country("C2", "Country2", "Kingdom of Country2", "EU",
                                                           10_000_000L);
        cc.setVisited(country2);
        assertFalse(cc.hasVisited(cc.new Country("JP", "Japan", "Japan", "Asia",
                                               126_000_000L)));
        assertTrue(cc.hasVisited(country2));
    }


}
