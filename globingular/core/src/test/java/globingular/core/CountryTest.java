package globingular.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryTest {
    static Country country0, country1, country2, country3, country4, country5;

    @BeforeAll
    public static void start() {
        country0 = new Country("_C0", "_country0", "Kingdom of Country0", "UN", "Asia", 123L);
        country1 = new Country("_C1", "_country1");
        country2 = new Country("_C2", "_country2");
        country3 = new Country("_C3", "_country3");
        country4 = new Country("_C4", "_country4");
        country5 = new Country("_C1", "_country1", "_country1", "UN", "", 0L);
    }

    @Test
    public void testConstructor() {
        Country c = new Country("_C0", "_country0", "Kingdom of Country0", "UN", "Asia", 123L);
        assertEquals("_C0", c.getCountryCode());
        assertEquals("_country0", c.getShortName());
        assertEquals("Kingdom of Country0", c.getLongName());
        assertEquals("UN", c.getSovereignty());
        assertEquals("Asia", c.getRegion());
        assertEquals(123L, c.getPopulation());
    }

    @Test
    public void testShortConstructor() {
        assertEquals(country1.getCountryCode(), country5.getCountryCode());
        assertEquals(country1.getLongName(), country5.getLongName());
        assertEquals(country1.getShortName(), country5.getShortName());
        assertEquals(country1.getSovereignty(), country5.getSovereignty());
        assertEquals(country1.getRegion(), country5.getRegion());
        assertEquals(country1.getPopulation(), country5.getPopulation());
    }
}
