package globingular.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {
    static Country country0, country1, country2, country3, country4;

    @BeforeAll
    public static void start() {
        country0 = new Country("_C0", "_country0", "Kingdom of Country0", "UN", "Asia", 123L, new Province[0]);
        country1 = new Country("_C1", "_country1");
        country2 = new Country("_C2", "_country2");
        country3 = new Country("_C3", "_country3");
        country4 = new Country("_C4", "_country4");
    }

    @Test
    public void testGetCountryFromCountryCode() {
        World world = new World(country0, country1, country2, country3);
        assertEquals(country2, world.getCountryFromCode(country2.getCountryCode()));
    }

    @Test
    public void testGetCountryFromCountryName() {
        World world = new World(country0, country1, country2, country3);
        assertEquals(country2, world.getCountryFromName(country2.getName()));
    }

    @Test
    public void testGetCountryFromCountryCodeFail() {
        World world = new World(country0, country1, country2, country3);
        assertNull(world.getCountryFromCode(country4.getCountryCode()));
    }

    @Test
    public void testGetCountryFromCountryNameFail() {
        World world = new World(country0, country1, country2, country3);
        assertNull(world.getCountryFromName(country4.getName()));
    }

    @Test
    public void testCountryExistsFalse() {
        World world = new World(country0, country1, country3);
        assertFalse(world.countryExists(country2));
    }

    @Test
    public void testCountryExistsTrue() {
        World world = new World(country0, country1, country3);
        assertTrue(world.countryExists(country1));
    }
}
