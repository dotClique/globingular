package globingular.persistence;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import globingular.core.CountryCollector;
import org.junit.jupiter.api.Test;

public class PersistenceHandlerTest {

    @Test
    public void testImportAll() {
        final PersistenceHandler p = new PersistenceHandler();
        CountryCollector cc = p.loadCountryCollector();
        assertNotNull(cc);
        assertNotNull(cc.getWorld());
        assertNotEquals(0, cc.getWorld().getCountries().size());
        assertNotNull(cc.getWorld().getCountryFromCode("NO"));
        assertNull(cc.getWorld().getCountryFromCode("hablebable"));
    }

    @Test
    public void testAutosaveDoesNotCrash() {
        final PersistenceHandler p = new PersistenceHandler();
        CountryCollector cc = p.loadCountryCollector();
        p.setAutosave(null, cc);
    }
}
