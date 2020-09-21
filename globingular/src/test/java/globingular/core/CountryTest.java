package globingular.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryTest {
    @Test
    public void testGetCountryFromCountryCode() {
        assertEquals(new Country("C5","Country5","ca","fs",0L), Country.getCountryFromCode("C5"));
    }

    @Test
    public void testGetCountryFromCountryName() {
        assertEquals(new Country("C6", "Country6","cd","lo",0L), Country.getCountryFromName("Country6"));
    }
}
