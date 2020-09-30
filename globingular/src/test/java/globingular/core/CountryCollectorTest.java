package globingular.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CountryCollectorTest {
    static Country country0, country1, country2, country3, country4;
    static World world0, world1, world2, world3, world4;

    @BeforeAll
    public static void start() {
        country0 = new Country("_C0", "_country0", "Kingdom of Country0", "UN", "Asia", 123L, new Province[0]);
        country1 = new Country("_C1", "_country1");
        country2 = new Country("_C2", "_country2");
        country3 = new Country("_C3", "_country3");
        country4 = new Country("_C4", "_country4");

        world0 = new World();
        world1 = new World(country0);
        world2 = new World(country0, country1);
        world3 = new World(country4, country3, country2);
        world4 = new World(country0, country1, country2, country3, country4);
    }

    @Test
    public void testToString() {
        CountryCollector cc = new CountryCollector(world4);
        cc.setVisited(country2);
        assertEquals("[" + country2.toString() + "]", cc.toString());
    }


    @Test
    public void testConstructor() {
        CountryCollector cc = new CountryCollector(world4);
        cc.setVisited(country1);
        cc.setVisited(country2);
        assertEquals(2, cc.numberVisited());
        assertTrue(cc.hasVisited(country2));
        assertTrue(cc.hasVisited(country1));
        assertFalse(cc.hasVisited(country3));
    }

    @Test
    public void testInitiallyEmpty() {
        CountryCollector cc = new CountryCollector(world4);
        assertEquals(0, cc.numberVisited());
        assertEquals("[]", cc.toString());
    }

    @Test
    public void testSetVisited() {
        CountryCollector cc = new CountryCollector(world3);
        assertFalse(cc.hasVisited(country3));
        cc.setVisited(country3);
        assertTrue(cc.hasVisited(country3));
    }

    @Test
    public void testRemoveVisited() {
        CountryCollector cc = new CountryCollector(world4);
        cc.setVisited(country2);
        assertTrue(cc.hasVisited(country2));
        cc.removeVisited(country2);
        assertFalse(cc.hasVisited(country2));
    }

}
