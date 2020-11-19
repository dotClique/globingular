package globingular.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import globingular.core.CountryCollector;
import globingular.core.GlobingularModule;
import globingular.persistence.PersistenceHandler;

public class CountryCollectorResourceTest {

    // The usernames used for testing
    private static String username = "hablebable";
    private static String usernameNew = username + "New";
    private static String usernameNewLower = usernameNew.toLowerCase();
    private static String usernameNewRenamed = usernameNewLower + "Renamed";
    private static String usernameOld = username + "Old";
    private static String usernameOldLower = usernameOld.toLowerCase();
    private static String usernameOldRenamed = usernameOldLower + "Renamed";
    private static String usernameOldRenamedLower = usernameOldRenamed.toLowerCase();
    private static String usernameTaken = username + "Taken";

    private GlobingularModule gModule;
    private CountryCollector cCollector1;
    private CountryCollector cCollector2;
    private PersistenceHandler pHandler;
    private CountryCollectorResource ccrNewUser;
    private CountryCollectorResource ccrOldUser;

    @BeforeEach
    public void beforeEach() throws IllegalArgumentException, IOException {
        // Mock init
        gModule = mock(GlobingularModule.class, new RuntimeExceptionAnswer());
        cCollector1 = mock(CountryCollector.class, new RuntimeExceptionAnswer());
        cCollector2 = mock(CountryCollector.class, new RuntimeExceptionAnswer());
        pHandler = mock(PersistenceHandler.class, new RuntimeExceptionAnswer());

        // Mock define
        // Using doReturn in order to use RuntimeExceptionAnswer() to report stubs not mocked
        doReturn(cCollector1).when(gModule).getCountryCollector(usernameNewLower);
        doReturn(null).when(gModule).getCountryCollector(usernameOldLower);

        // Using any() as all put and remove operations should complete
        doReturn(true).when(gModule).putCountryCollector(anyString(), any());
        doReturn(true).when(gModule).removeCountryCollector(any());

        // Configure return for all possible usernames
        doReturn(true).when(gModule).isUsernameAvailable(endsWith("new"));
        doReturn(true).when(gModule).isUsernameAvailable(endsWith("renamed"));
        doReturn(false).when(gModule).isUsernameAvailable(endsWith("old"));
        doReturn(false).when(gModule).isUsernameAvailable(endsWith("taken"));

        // Always accept saving
        doReturn(true).when(pHandler).saveState(any(String.class), any());

        ccrNewUser =  new CountryCollectorResource(gModule, usernameNew, null, pHandler);
        ccrOldUser =  new CountryCollectorResource(gModule, usernameOld, cCollector1, pHandler);

        // Verify that interaction-counter starts at 0, so that we don't need to check for 0 at start of each test
        verifyNoInteractions(gModule);
        verifyNoInteractions(cCollector1);
        verifyNoInteractions(cCollector2);
        verifyNoInteractions(pHandler);
    }

    /**
     * Helper method to avoid duplicating code too much.
     * Verifies that {@link PersistenceHandler#saveState(String, CountryCollector)} has been called
     * the given amount of times for the parameters username and countryCollector.
     * 
     * @param username                  The username to count for
     * @param countryCollector          The countryCollector to count for
     * @param times                     The amount of times to check, usually 1
     * @throws IllegalArgumentException If username is invalid: {@link GlobingularModule#isUsernameValid(String)}
     * @throws IOException              If saving fails (not really gonna happen, as it's a mocked instance)
     */
    private void internalTestSaveAppState(final String username, final CountryCollector countryCollector,
            final int times) throws IllegalArgumentException, IOException {
        verify(pHandler, times(times)).saveState(username, countryCollector);
    }

    @Test
    public void testPutCountryCollectorForNewUser() throws IllegalArgumentException, IOException {
        assertEquals(true, ccrNewUser.putCountryCollector(cCollector1));
        verify(gModule).putCountryCollector(usernameNewLower, cCollector1);
        internalTestSaveAppState(usernameNewLower, cCollector1, 1);
    }

    @Test
    public void testPutCountryCollectorForOldUser() throws IllegalArgumentException, IOException {
        assertEquals(true, ccrOldUser.putCountryCollector(cCollector2));
        verify(gModule).putCountryCollector(usernameOldLower, cCollector2);
        internalTestSaveAppState(usernameOldLower, cCollector2, 1);
    }

    @Test
    public void testExceptionOnPutCountryCollectorWithNullForNewUser() throws IOException {
        try {
            ccrNewUser.putCountryCollector(null);
            fail("Should have thrown WebApplicationException");
        } catch (RuntimeException e) {
            // Success - we catch RuntimeException as WebApplicationException fails when not run as server
        }
    }

    @Test
    public void testExceptionOnPutCountryCollectorWithNullForOldUser() throws IOException {
        try {
            ccrOldUser.putCountryCollector(null);
            fail("Should have thrown WebApplicationException");
        } catch (RuntimeException e) {
            // Success - we catch RuntimeException as WebApplicationException fails when not run as server
        }
    }

    @Test
    public void testDeleteCountryCollectorForNewUser() throws IllegalArgumentException, IOException {
        assertEquals(true, ccrNewUser.deleteCountryCollector());
        verify(gModule).removeCountryCollector(usernameNewLower);
        internalTestSaveAppState(usernameNewLower, null, 1);
    }

    @Test
    public void testDeleteCountryCollectorForOldUser() throws IOException {
        assertEquals(true, ccrOldUser.deleteCountryCollector());
        verify(gModule).removeCountryCollector(usernameOldLower);
        internalTestSaveAppState(usernameOldLower, null, 1);
    }

    @Test
    public void testRenameCountryCollectorForNewUser() throws IllegalArgumentException, IOException {
        assertEquals(false, ccrNewUser.renameCountryCollector(usernameNewRenamed));
        verify(gModule).isUsernameAvailable(usernameNewLower);
    }

    @Test
    public void testRenameCountryCollectorForOldUser() throws IllegalArgumentException, IOException {
        assertEquals(true, ccrOldUser.renameCountryCollector(usernameOldRenamed));
        verify(gModule).putCountryCollector(usernameOldRenamedLower, cCollector1);
        verify(gModule).removeCountryCollector(usernameOldLower);
        internalTestSaveAppState(usernameOldLower, null, 1);
        internalTestSaveAppState(usernameOldRenamedLower, cCollector1, 1);
    }

    @Test
    public void testExceptionOnRenameCountryCollectorToUnavailableUsername() throws IOException {
        try {
            ccrOldUser.renameCountryCollector(usernameTaken);
            fail("Should have thrown WebApplicationException");
        } catch (RuntimeException e) {
            // Success - we catch RuntimeException as WebApplicationException fails when not run as server
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
