package globingular.core;

import javafx.beans.property.ReadOnlySetProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collection;

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
        country0 = new Country("_C0", "_country0", "Kingdom of Country0", "UN", "Asia", 123L, new Province[0]);
        country1 = new Country("_C1", "_country1");
        country2 = new Country("_C2", "_country2");
        country3 = new Country("_C3", "_country3");
        country4 = new Country("_C4", "_country4");

        world0 = new World();
        world1 = new World(country0);
        world2 = new World(country0, country1);
        world3 = new World(country4, country3, country2);
        world4 = new World(country0, country1, country2, country3, country4);

        visit0 = new Visit(country0, LocalDateTime.now(), LocalDateTime.now());
        visit1 = new Visit(country1, LocalDateTime.now(), LocalDateTime.now());
        visit2 = new Visit(country2, LocalDateTime.now(), LocalDateTime.now());
        visit3 = new Visit(country3, LocalDateTime.now(), LocalDateTime.now());
        visit4 = new Visit(country4, LocalDateTime.now(), LocalDateTime.now());

        visit0_2 = new Visit(country0, LocalDateTime.of(2020, 01, 01, 12, 0, 0), LocalDateTime.of(2020, 01, 31, 12, 0, 0));
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
        String s1 = cc.toString();
        String s2 = "[" + visit2.toString() + ", " + visit3.toString() + "]";
        String s3 = "[" + visit3.toString() + ", " + visit2.toString() + "]";
        assertTrue(s1.equals(s2) || s1.equals(s3));
    }

    @Test
    public void testConstructor() {
        CountryCollector cc = new CountryCollector(world4);
        cc.registerVisit(country1);
        cc.registerVisit(country2);
        assertEquals(2, cc.numberVisited());
        assertTrue(cc.isVisited(country2));
        assertTrue(cc.isVisited(country1));
        assertFalse(cc.isVisited(country3));
    }

    @Test
    public void testInitiallyEmpty() {
        CountryCollector cc = new CountryCollector(world4);
        assertEquals(0, cc.numberVisited());
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
    public void testRegisterVisitWithCountryWithoutArrival() {
        CountryCollector cc = new CountryCollector(world3);
        assertFalse(cc.isVisited(country3));
        cc.registerVisit(country3);
        assertTrue(cc.isVisited(country3));
    }

    @Test
    public void testRegisterVisitWithCountryAndTimeRange() {
        LocalDateTime dt = LocalDateTime.of(2020, 01, 01, 12, 0, 0);
        LocalDateTime dt_2 = LocalDateTime.of(2020, 01, 31, 12, 0, 0);

        CountryCollector cc = new CountryCollector(world3);
        assertFalse(cc.isVisited(country3));
        cc.registerVisit(country3, dt, dt_2);
        assertTrue(cc.isVisited(country3));
        
        Collection<Visit> visits = cc.getCountryVisits(country3);
        assertTrue(visits.stream().anyMatch(v -> v.getArrival() == dt));
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
    public void testVisitsPropertyIsReadOnly() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.visitsProperty().add(visit0);
            fail("Returned set is writable");
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testGetVisitsIsReadOnly() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.getVisits().add(visit0);
            fail("Returned set is writable");
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testVisitsPropertyIsSynchronized() {
        CountryCollector cc = new CountryCollector(world1);
        ReadOnlySetProperty<Visit> testSync = cc.visitsProperty();
        cc.registerVisit(country0);
        assertEquals(1, testSync.size());
    }

    @Test
    public void testGetVisitsIsSynchronized() {
        CountryCollector cc = new CountryCollector(world1);
        ObservableSet<Visit> testSync = cc.getVisits();
        cc.registerVisit(country0);
        assertEquals(1, testSync.size());
    }

    @Test
    public void testVisitedCountriesPropertyIsReadOnly() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.visitedCountriesProperty().add(country0);
            fail("Returned set is writable");
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testGetVisitedCountriesIsReadOnly() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.getVisitedCountries().add(country0);
            fail("Returned set is writable");
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testGetVisitedCountriesSortedIsReadOnly() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.getVisitedCountriesSorted().add(country0);
            fail("Returned list is writable");
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testVisitedCountriesPropertyIsSynchronized() {
        CountryCollector cc = new CountryCollector(world1);
        ReadOnlySetProperty<Country> testSync = cc.visitedCountriesProperty();
        cc.registerVisit(country0);
        assertEquals(1, testSync.size());
    }

    @Test
    public void testGetVisitedCountriesSortedIsSynchronized() {
        CountryCollector cc = new CountryCollector(world1);
        ObservableList<Country> testSync = cc.getVisitedCountriesSorted();
        cc.registerVisit(country0);
        assertEquals(1, testSync.size());
    }

    @Test
    public void testGetVisitedCountriesIsSynchronized() {
        CountryCollector cc = new CountryCollector(world1);
        ObservableSet<Country> testSync = cc.getVisitedCountries();
        cc.registerVisit(country0);
        assertEquals(1, testSync.size());
    }
}
