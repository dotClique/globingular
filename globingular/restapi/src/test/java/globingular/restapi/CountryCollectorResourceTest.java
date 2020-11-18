package globingular.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.persistence.PersistenceHandler;

public class CountryCollectorResourceTest {

    private String usernameNew = "hablebableNew";
    private String usernameNewRenamed = usernameNew + "Renamed";
    private String usernameOld = "hablebableOld";
    private String usernameOldRenamed = usernameOld + "Renamed";
    private String usernameTaken = "habebableTaken";
    private GlobingularModule gModule;
    private CountryCollector cCollector1;
    private CountryCollector cCollector2;
    private PersistenceHandler pHandler;
    private CountryCollectorResource ccrNewUser;
    private CountryCollectorResource ccrOldUser;

    @BeforeEach
    public void beforeEach() {
        // Mock init
        gModule = mock(GlobingularModule.class, new RuntimeExceptionAnswer());
        cCollector1 = mock(CountryCollector.class, new RuntimeExceptionAnswer());
        cCollector2 = mock(CountryCollector.class, new RuntimeExceptionAnswer());
        pHandler = mock(PersistenceHandler.class, new RuntimeExceptionAnswer());

        // Mock define
        // Using doReturn in order to use RuntimeExceptionAnswer() to report stubs not mocked
        doReturn(cCollector1).when(gModule).getCountryCollector(usernameNew);
        doReturn(null).when(gModule).getCountryCollector(usernameOld);

        doReturn(true).when(gModule).isUsernameAvailable(usernameNew);
        doReturn(false).when(gModule).isUsernameAvailable(usernameOld);

        // Using any() as all put and remove operations should complete
        doReturn(true).when(gModule).putCountryCollector(anyString(), any());
        doReturn(true).when(gModule).removeCountryCollector(any());

        for (String s : new String[]{usernameNew, usernameNewRenamed, usernameOldRenamed}) {
            doReturn(true).when(gModule).isUsernameAvailable(s);
        }
        for (String s : new String[]{usernameOld, usernameTaken}) {
            doReturn(false).when(gModule).isUsernameAvailable(s);
        }

        doReturn(true).when(pHandler).saveState(any(String.class), any());

        ccrNewUser =  new CountryCollectorResource(gModule, usernameNew, null, pHandler);
        ccrOldUser =  new CountryCollectorResource(gModule, usernameOld, cCollector1, pHandler);

        verifyNoInteractions(gModule);
        verifyNoInteractions(cCollector1);
        verifyNoInteractions(cCollector2);
        verifyNoInteractions(pHandler);
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

    @Test
    public void testRenameCountryCollectorForOldUser() {
        assertEquals(true, ccrOldUser.renameCountryCollector(usernameOldRenamed));
        verify(gModule, times(1)).putCountryCollector(usernameOldRenamed, cCollector1);
        verify(gModule, times(1)).removeCountryCollector(usernameOld);
        internalTestSaveAppState(usernameOld, null, 1);
        internalTestSaveAppState(usernameOldRenamed, cCollector1, 1);
    }

    @Test
    public void testExceptionOnRenameCountryCollectorToUnavailableUsername() {
        try {
            ccrOldUser.renameCountryCollector(usernameTaken);
            fail("Should have thrown Illegal Argument Exception");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * Custom error message for when a method is called on a Mocked instance, that is not stubbed (aka. not handled).
     */
    private static class RuntimeExceptionAnswer implements Answer<Object> {
        public Object answer(final InvocationOnMock invocation) throws Throwable {
            throw new RuntimeException(invocation.getMethod().getName() + " is not stubbed");
        }
    }
}
