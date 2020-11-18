package globingular.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.persistence.PersistenceHandler;

public class CountryCollectorResourceTest {

    private String usernameNew = "hablebableNew";
    private String usernameNewRenamed = usernameNew + "Renamed";
    private String usernameOld = "hablebableOld";
    private String usernameOldRenamed = usernameOld + "Renamed";
    private GlobingularModule gModule;
    private CountryCollector cCollector1;
    private CountryCollector cCollector2;
    private PersistenceHandler pHandler;
    private CountryCollectorResource ccrNewUser;
    private CountryCollectorResource ccrOldUser;

    @BeforeEach
    public void beforeEach() {
        // Mock init
        gModule = mock(GlobingularModule.class);
        cCollector1 = mock(CountryCollector.class);
        cCollector2 = mock(CountryCollector.class);
        pHandler = mock(PersistenceHandler.class);

        // Mock define
        when(gModule.getCountryCollector(usernameNew)).thenReturn(cCollector1);
        when(gModule.getCountryCollector(usernameOld)).thenReturn(null);

        when(gModule.isUsernameAvailable(usernameNew)).thenReturn(false);
        when(gModule.isUsernameAvailable(usernameOld)).thenReturn(true);

        when(gModule.putCountryCollector(usernameNew, cCollector1)).thenReturn(true);
        when(gModule.putCountryCollector(usernameOld, cCollector1)).thenReturn(true);
        when(gModule.putCountryCollector(usernameOld, cCollector2)).thenReturn(true);

        when(gModule.removeCountryCollector(usernameNew)).thenReturn(true);
        when(gModule.removeCountryCollector(usernameOld)).thenReturn(true);

        when(gModule.isUsernameAvailable(usernameNew)).thenReturn(true);
        when(gModule.isUsernameAvailable(usernameNewRenamed)).thenReturn(true);
        when(gModule.isUsernameAvailable(usernameOld)).thenReturn(false);
        when(gModule.isUsernameAvailable(usernameOldRenamed)).thenReturn(true);

        ccrNewUser =  new CountryCollectorResource(gModule, usernameNew, null, pHandler);
        ccrOldUser =  new CountryCollectorResource(gModule, usernameOld, cCollector1, pHandler);

        verifyZeroInteractions(gModule);
        verifyZeroInteractions(cCollector1);
        verifyZeroInteractions(cCollector2);
        verifyZeroInteractions(pHandler);
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(gModule);
        verifyNoMoreInteractions(cCollector1);
        verifyNoMoreInteractions(cCollector2);
        verifyNoMoreInteractions(pHandler);
    }

    private void internalTestSaveAppState(final String username, final CountryCollector countryCollector,
            final int times) {
        verify(pHandler, times(times)).saveState(username, countryCollector);
    }

    @Test
    public void testPutCountryCollectorForNewUser() {
        assertEquals(true, ccrNewUser.putCountryCollector(cCollector1));
        verify(gModule, times(1)).putCountryCollector(usernameNew, cCollector1);
        internalTestSaveAppState(usernameNew, cCollector1, 1);
    }

    @Test
    public void testPutCountryCollectorForOldUser() {
        assertEquals(true, ccrOldUser.putCountryCollector(cCollector2));
        verify(gModule, times(1)).putCountryCollector(usernameOld, cCollector2);
        internalTestSaveAppState(usernameOld, cCollector2, 1);
    }

    @Test
    public void testExceptionOnPutCountryCollectorWithNullForNewUser() {
        try {
            ccrNewUser.putCountryCollector(null);
            fail("Should have thrown Illegal Argument Exception");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testExceptionOnPutCountryCollectorWithNullForOldUser() {
        try {
            ccrOldUser.putCountryCollector(null);
            fail("Should have thrown Illegal Argument Exception");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testDeleteCountryCollectorForNewUser() {
        assertEquals(true, ccrNewUser.deleteCountryCollector());
        verify(gModule, times(1)).removeCountryCollector(usernameNew);
        internalTestSaveAppState(usernameNew, null, 1);
    }

    @Test
    public void testDeleteCountryCollectorForOldUser() {
        assertEquals(true, ccrOldUser.deleteCountryCollector());
        verify(gModule, times(1)).removeCountryCollector(usernameOld);
        internalTestSaveAppState(usernameOld, null, 1);
    }

    @Test
    public void testRenameCountryCollectorForNewUser() {
        assertEquals(false, ccrNewUser.renameCountryCollector(usernameNewRenamed));
        verify(gModule, times(1)).isUsernameAvailable(usernameNew);
    }
}
