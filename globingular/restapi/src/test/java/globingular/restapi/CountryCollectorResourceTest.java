package globingular.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.persistence.PersistenceHandler;

public class CountryCollectorResourceTest {

    private String username1 ="hablebable1";
    private String username2 ="hablebable2";
    private GlobingularModule module;
    private CountryCollector collector;
    private PersistenceHandler persistence;
    private CountryCollectorResource ccr1;
    private CountryCollectorResource ccr2;

    /**
     * Sets up mocking of Core-classes.
     */
    @BeforeEach
    public void start() {
        // Mock init
        module = mock(GlobingularModule.class);
        collector = mock(CountryCollector.class);
        persistence = null;

        // Mock define
        when(module.getCountryCollector(username1)).thenReturn(collector);
        when(module.isUsernameAvailable(username1)).thenReturn(false);

        when(module.getCountryCollector(username2)).thenReturn(null);
        when(module.isUsernameAvailable(username2)).thenReturn(true);
        when(module.putCountryCollector(username2, collector)).thenReturn(true);

        // Create CCR-instance with valid username-collector pair
        ccr1 = new CountryCollectorResource(module, username1, collector, persistence);

        // Create CCR-instance with username without a collector
        ccr2 = new CountryCollectorResource(module, username2, null, persistence);
    }

    /**
     * Tests that a simple get-call works for a valid username.
     */
    @Test
    public void testGetCountryCollectorForValidUsername() {
        assertEquals(collector, ccr1.getCountryCollector());
    }

    /**
     * Tests that exception is thrown when asking for a nonexisting user.
     */
    @Test
    public void testGetCountryCollectorForInvalidUsername() {
        assertNull(ccr2.getCountryCollector());
    }

    /**
     * Tests that a user won't be overwritten with an empty CC-instance.
     */
    @Test
    public void testPutCountryCollectorForUnavailableUsername() {
        assertEquals(false, ccr1.putCountryCollector(collector));
    }

    /**
     * Tests that an empty CC-instance can be saved when the username is available.
     */
    @Test
    public void testPutCountryCollectorForAvailableUsername() {
        assertEquals(true, ccr2.putCountryCollector(collector));
    }
}
