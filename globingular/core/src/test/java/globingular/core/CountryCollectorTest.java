package globingular.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <p>JUnit test class that tests the following:
 * <ul>
 * <li>toString</li>
 * <li>constructor</li>
 * <li>empty constructor</li>
 * <li>register visit to a country</li>
 * <li>remove a visit to a country</li>
 * <li>has visited country</li>
 * <li>encapulation/readOnly of the private sets</li>
 * </ul>
 * </p>
 */
public class CountryCollectorTest {
    static Country country0, country1, country2, country3, country4;
    static World world0, world1, world2, world3, world4;
    static Visit visit0, visit0_2, visit1, visit2, visit3, visit4;

    @BeforeAll
    public static void start() {
        country0 = new Country("_C0", "_country0", "Kingdom of Country0", "UN", "Asia", 123L);
        country1 = new Country("_C1", "_country1");
        country2 = new Country("_C2", "_country2");
        country3 = new Country("_C3", "_country3");
        country4 = new Country("_C4", "_country4");

        world0 = new World();
        world1 = new World(country0);
        world2 = new World(country0, country1);
        world3 = new World(country4, country3, country2);
        world4 = new World(country0, country1, country2, country3, country4);

        visit0 = new Visit(country0, LocalDate.now(), LocalDate.now());
        visit1 = new Visit(country1, LocalDate.now(), LocalDate.now());
        visit2 = new Visit(country2, LocalDate.now(), LocalDate.now());
        visit3 = new Visit(country3, LocalDate.now(), LocalDate.now());
        visit4 = new Visit(country4, LocalDate.now(), LocalDate.now());

        visit0_2 = new Visit(country0,
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 31));
    }

    @Test
    public void testToString() {
        CountryCollector cc = new CountryCollector(world4);
        cc.registerVisit(visit2);
        assertEquals("[" + visit2.toString() + "]", cc.toString());
    }

    @Test
    public void testToStrings() {
        CountryCollector cc = new CountryCollector(world4);
        cc.registerVisit(visit2);
        cc.registerVisit(visit3);
        // Can't guarantee order
        assertTrue(cc.toString().equals("[" + visit2.toString() + ", " + visit3.toString() + "]")
                || cc.toString().equals("[" + visit3.toString() + ", " + visit2.toString() + "]"));
    }

    @Test
    public void testConstructor() {
        CountryCollector cc = new CountryCollector(world4);
        cc.registerVisit(country1);
        cc.registerVisit(country2);
        assertEquals(2, cc.numberOfCountriesVisited());
        assertTrue(cc.isVisited(country2));
        assertTrue(cc.isVisited(country1));
        assertFalse(cc.isVisited(country3));
    }

    @Test
    public void testInitiallyEmpty() {
        CountryCollector cc = new CountryCollector(world4);
        assertEquals(0, cc.numberOfCountriesVisited());
        assertEquals("[]", cc.toString());
    }

    @Test
    public void testRegisterVisitWithVisit() {
        CountryCollector cc = new CountryCollector(world3);
        assertFalse(cc.isVisited(country3));
        cc.registerVisit(visit3);
        assertTrue(cc.isVisited(country3));
    }

    @Test
    public void testRegisterVisitWithCountryWithoutTimeRange() {
        CountryCollector cc = new CountryCollector(world3);
        assertFalse(cc.isVisited(country3));
        cc.registerVisit(country3);
        assertTrue(cc.isVisited(country3));

        // Test to make sure that arrival and departure is set to null
        Collection<Visit> visits = cc.getVisitsToCountry(country3);
        assertTrue(visits.stream().anyMatch(v -> v.getArrival() == null && v.getDeparture() == null));
    }

    @Test
    public void testRegisterVisitWithCountryAndTimeRange() {
        LocalDate ld1 = LocalDate.of(2020, 1, 1);
        LocalDate ld2 = LocalDate.of(2020, 1, 31);

        CountryCollector cc = new CountryCollector(world3);
        assertFalse(cc.isVisited(country3));
        cc.registerVisit(country3, ld1, ld2);
        assertTrue(cc.isVisited(country3));

        Collection<Visit> visits = cc.getVisitsToCountry(country3);
        assertTrue(visits.stream().anyMatch(v -> v.getArrival() == ld1));
    }

    @Test
    public void testRegisterMultipleVisitsToSameCountry() {
        CountryCollector cc = new CountryCollector(world2);
        cc.registerVisit(visit0);
        cc.registerVisit(visit0_2);
        assertTrue(cc.getVisits().contains(visit0));
        assertTrue(cc.getVisits().contains(visit0_2));
    }

    @Test
    public void testRemoveVisit() {
        CountryCollector cc = new CountryCollector(world3);
        cc.registerVisit(visit2);
        assertTrue(cc.isVisited(country2));
        cc.removeVisit(visit2);
        assertFalse(cc.isVisited(country2));
    }

    @Test
    public void testRemoveAllVisitsToCountry() {
        CountryCollector cc = new CountryCollector(world4);
        cc.registerVisit(country2);
        assertTrue(cc.isVisited(country2));
        cc.removeAllVisitsToCountry(country2);
        assertFalse(cc.isVisited(country2));
    }

    @Test
    public void testExceptionOnRegisterUnknownCountryVisit() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.registerVisit(country1);
            fail("No exception thrown for attempted marking of country as visited even though country "
                    + "is not part of this collector's World");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testExceptionOnRemoveAllVisitsToUnknownCountry() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.removeAllVisitsToCountry(country1);
            fail("No exception thrown for attempted removing of visits to country even though country "
                    + "is not part of this collector's World");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testExceptionOnRemoveVisitToUnknownCountry() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.removeVisit(visit1);
            fail("No exception thrown for attempted removal of a visit even though country "
                    + "is not part of this collector's World");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testExceptionOnCheckUnknownCountryIsVisited() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.isVisited(country1);
            fail("No exception thrown for attempted checking of country's visited-status even though country "
                    + "is not part of this collector's World");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testGetVisitsToCountry() {
        CountryCollector cc = new CountryCollector(world4);
        cc.registerVisit(country1);
        cc.registerVisit(country1);
        cc.registerVisit(visit1);
        cc.registerVisit(country2);
        cc.registerVisit(country2);
        cc.registerVisit(visit2);
        cc.registerVisit(country3);
        cc.registerVisit(visit3);

        Collection<Visit> arr1 = cc.getVisitsToCountry(country2);
        assertTrue(arr1.stream().allMatch(v -> v.getCountry() == country2),
                "The returned collection didn't only contain visits with the correct country");

        Collection<Visit> arr2 =
                cc.getVisits().stream().filter(v -> v.getCountry() == country2).collect(Collectors.toList());
        assertEquals(arr2, arr1, "The returned collection doesn't contain all the visits to the given country");
    }

    @Test
    public void testGetVisitsIsUnmodifiable() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.getVisits().add(visit0);
            fail("Returned set is modifiable");
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    public void testGetVisitedCountriesIsUnmodifiable() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.getVisitedCountries().add(country0);
            fail("Returned set is modifiable");
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    public void testListenerIsProperlyNotified() {
        CountryCollector cc = new CountryCollector(world1);
        @SuppressWarnings("unchecked")
        Listener<Visit> listener = (Listener<Visit>) mock(Listener.class);

        cc.addListener(listener);
        Visit visit = new Visit(country0, null, null);
        cc.registerVisit(visit);
        verify(listener).notifyListener(eq(new ChangeEvent<>(ChangeEvent.Status.ADDED, visit)));
    }

    @Test
    public void testRemoveListener() {
        CountryCollector cc = new CountryCollector(world1);
        @SuppressWarnings("unchecked")
        Listener<Visit> listener = (Listener<Visit>) mock(Listener.class);

        cc.addListener(listener);
        Visit visit = new Visit(country0, null, null);
        cc.registerVisit(visit);
        verify(listener).notifyListener(eq(new ChangeEvent<>(ChangeEvent.Status.ADDED, visit)));

        cc.removeListener(listener);
        assertTrue(cc.getListeners().isEmpty());
        cc.removeVisit(visit);
        verify(listener).notifyListener(eq(new ChangeEvent<>(ChangeEvent.Status.ADDED, visit)));
    }
}
