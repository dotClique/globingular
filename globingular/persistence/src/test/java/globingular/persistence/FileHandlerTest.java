package globingular.persistence;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.core.World;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
    public void testSaveCountryCollector() {
        Country country = new Country("NO","Norway");
        CountryCollector cc = new CountryCollector(new World(country));
        LocalDateTime ldt1 = LocalDateTime.of(2000,12,21,12,0);
        LocalDateTime ldt2 = LocalDateTime.of(2000,12,21,12,14);
        cc.registerVisit(new Visit(country, ldt1, ldt2));

        FileHandler.saveCountryCollector(new PersistenceHandler(), "testSaveCountryCollector", cc);
    }

    @Test
    public void testSaveAndLoadCountryCollector() {
        Country country = new Country("NO","Norway");
        CountryCollector cc = new CountryCollector(new World(country));
        LocalDateTime ldt1 = LocalDateTime.of(2000,12,21,12,0);
        LocalDateTime ldt2 = LocalDateTime.of(2000,12,21,12,14);
        cc.registerVisit(new Visit(country, ldt1, ldt2));

        FileHandler.saveCountryCollector(new PersistenceHandler(), "testSaveAndLoadCountryCollector", cc);
        CountryCollector cc2 = FileHandler.loadCountryCollector(new PersistenceHandler(), "testSaveAndLoadCountryCollector");
        assertNotEquals(cc, cc2);
    }

    @Test
    public void testSaveCountryCollectorWithUsernameWithSpaceFails() {
        try {
            FileHandler.saveCountryCollector(new PersistenceHandler(), "h i", new CountryCollector(new World()));
            fail("Was allowed to save with username containing spaces");
        } catch (IllegalArgumentException ignored) {

        }
    }
}
