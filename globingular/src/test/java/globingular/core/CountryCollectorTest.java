package globingular.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CountryCollectorTest {

    private static Country country1, country2, country3;

    @BeforeAll
    public static void start() {
        country1 = new Country("C1", "Country1", "Kingdom of Country1", "UN",
                               "EU", 5_000_000L, new Province[]{});
        country2 = new Country("C2", "Country2", "Kingdom of Country2", "EU",
                               10_000_000L);
        country3 = new Country("C3", "Country3", "Kingdom of Country3", "UN",
                               47_431_256L);
    }

    @Test
    public void testToString() {
        CountryCollector cc2 = new CountryCollector(country2);
        assertEquals(
                "[Country{countryCode='C2', name='Country2', longname='Kingdom of Country2', sovereignty='UN', " +
                "region='EU', population=10000000, provinces=[]}]",
                cc2.toString());
    }

    @Test
    public void testConstructor() {
        CountryCollector cc2 = new CountryCollector(country1, country2);
        assertEquals(2, cc2.numberVisited());
        assertTrue(cc2.hasVisited(country2));
        assertTrue(cc2.hasVisited(country1));
        assertFalse(cc2.hasVisited(country3));
        assertFalse(cc2.hasVisited(new Country("C4", "Country4", "Kingdom of Country4", "EU", 0L)));
    }

    @Test
    public void testEmptyConstructor() {
        CountryCollector cc2 = new CountryCollector();
        assertEquals(0, cc2.numberVisited());
        assertEquals("[]", cc2.toString());
    }

    @Test
    public void testSetVisited() {
        CountryCollector cc2 = new CountryCollector();
        assertFalse(cc2.hasVisited(country3));
        cc2.setVisited(country3);
        assertTrue(cc2.hasVisited(country3));
    }

    @Test
    public void testRemoveVisited() {
        CountryCollector cc2 = new CountryCollector(country2);
        assertTrue(cc2.hasVisited(country2));
        cc2.removeVisited(country2);
        assertFalse(cc2.hasVisited(country2));
    }

    @Test
    public void testHasVisited() {
        CountryCollector cc2 = new CountryCollector(country2);
        assertFalse(cc2.hasVisited(new Country("JP", "Japan", "Japan", "Asia",
                                              126_000_000L)));
        assertTrue(cc2.hasVisited(country2));
    }


}
