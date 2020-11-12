package globingular.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {
    static Country country0, country1, country2, country3, country4;

    @BeforeAll
    public static void start() {
        country0 = new Country("_C0", "_country0", "Kingdom of Country0", "UN", "Asia", 123L);
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
        assertEquals(country2, world.getCountryFromName(country2.getShortName()));
    }

    @Test
    public void testGetCountryFromCountryCodeFail() {
        World world = new World(country0, country1, country2, country3);
        assertNull(world.getCountryFromCode(country4.getCountryCode()));
    }

    @Test
    public void testGetCountryFromCountryNameFail() {
        World world = new World(country0, country1, country2, country3);
        assertNull(world.getCountryFromName(country4.getShortName()));
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

    @Test
    public void testExceptionOnDuplicateCountryCode() {
        try {
            new World(country0, country1, country3, new Country("_C0", "__testname"));
            fail("No exception thrown for attempted construction of World with two countries duplicating a countryCode");
        } catch (DuplicateIdentifierException ignored) {
        }
    }

    @Test
    public void testExceptionOnDuplicateCountryShortName() {
        try {
            new World(country0, country1, country3, new Country("__testcountrycode", "_country0"));
            fail("No exception thrown for attempted construction of World with two countries duplicating a short name");
        } catch (DuplicateIdentifierException ignored) {
        }
    }

    @Test
    public void testExceptionOnDuplicateCountry() {
        try {
            new World(country0, country1, country3, country0);
            fail("No exception thrown for attempted construction of World with duplicate countries");
        } catch (DuplicateIdentifierException ignored) {
        }
    }

    @Test
    public void testGetCountries() {
        World world = new World(country0, country1, country3);
        Set<Country> countries = world.getCountries();
        assertEquals(Set.of(country0, country1, country3), countries);
    }
}
