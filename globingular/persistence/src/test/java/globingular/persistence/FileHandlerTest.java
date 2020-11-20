package globingular.persistence;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.core.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link FileHandler}.
 */
public class FileHandlerTest {
    @Test
    public void testLoadDefaultWorld() {
        World world = FileHandler.loadPredominantDefaultWorld();
        assertNotNull(world);
        assertNotEquals(0, world.getCountries().size());
        assertNotNull(world.getCountryFromCode("NO"));
        assertNull(world.getCountryFromCode("hablebable"));
    }

    @Test
    public void testSaveCountryCollector() throws IOException {
        Country country = new Country("NO","Norway");
        CountryCollector cc = new CountryCollector(new World(country));
        LocalDate ld1 = LocalDate.of(2000,12,21);
        LocalDate ld2 = LocalDate.of(2000,12,21);
        cc.registerVisit(new Visit(country, ld1, ld2));

        FileHandler.saveCountryCollector(new PersistenceHandler(), "testsavecountrycollector", cc);
    }

    @Test
    public void testSaveAndLoadCountryCollector() throws IOException {
        Country country = new Country("NO","Norway");
        CountryCollector cc = new CountryCollector(new World(country));
        LocalDate ld1 = LocalDate.of(2000,12,21);
        LocalDate ld2 = LocalDate.of(2000,12,21);
        cc.registerVisit(new Visit(country, ld1, ld2));

        FileHandler.saveCountryCollector(new PersistenceHandler(), "testsaveandloadcountrycollector", cc);
        CountryCollector cc2 = FileHandler.loadCountryCollector(new PersistenceHandler(), "testsaveandloadcountrycollector");
        assertNotEquals(cc, cc2);
    }

    @Test
    public void testSaveCountryCollectorWithUsernameWithSpaceFails() {
        try {
            FileHandler.saveCountryCollector(new PersistenceHandler(), "h i", new CountryCollector(new World()));
            fail("Was allowed to save with username containing spaces");
        } catch (IllegalArgumentException | IOException ignored) {

        }
    }
}
