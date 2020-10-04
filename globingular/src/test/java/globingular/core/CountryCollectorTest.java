package globingular.core;

import javafx.beans.property.ReadOnlySetProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CountryCollectorTest {
    static Country country0, country1, country2, country3, country4;
    static World world0, world1, world2, world3, world4;

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
    }

    @Test
    public void testToString() {
        CountryCollector cc = new CountryCollector(world4);
        cc.setVisited(country2);
        assertEquals("[" + country2.toString() + "]", cc.toString());
    }


    @Test
    public void testConstructor() {
        CountryCollector cc = new CountryCollector(world4);
        cc.setVisited(country1);
        cc.setVisited(country2);
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
    public void testSetVisited() {
        CountryCollector cc = new CountryCollector(world3);
        assertFalse(cc.isVisited(country3));
        cc.setVisited(country3);
        assertTrue(cc.isVisited(country3));
    }

    @Test
    public void testRemoveVisited() {
        CountryCollector cc = new CountryCollector(world4);
        cc.setVisited(country2);
        assertTrue(cc.isVisited(country2));
        cc.removeVisited(country2);
        assertFalse(cc.isVisited(country2));
    }

    @Test
    public void testExceptionOnSetUnknownCountryVisited() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.setVisited(country1);
            fail("No exception thrown for attempted marking of country as visited even though country "
                         + "is not part of this collector's World");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testExceptionOnRemoveUnknownCountryVisited() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.setVisited(country1);
            fail("No exception thrown for attempted removal of marking of country as visited even though country "
                         + "is not part of this collector's World");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testExceptionOnCheckUnknownCountryIsVisited() {
        CountryCollector cc = new CountryCollector(world1);
        try {
            cc.setVisited(country1);
            fail("No exception thrown for attempted checking of country's visited-status even though country "
                         + "is not part of this collector's World");
        } catch (IllegalArgumentException ignored) {
        }
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
        cc.setVisited(country0);
        assertEquals(1, testSync.size());
    }

    @Test
    public void testGetVisitedCountriesSortedIsSynchronized() {
        CountryCollector cc = new CountryCollector(world1);
        ObservableList<Country> testSync = cc.getVisitedCountriesSorted();
        cc.setVisited(country0);
        assertEquals(1, testSync.size());
    }

    @Test
    public void testGetVisitedCountriesIsSynchronized() {
        CountryCollector cc = new CountryCollector(world1);
        ObservableSet<Country> testSync = cc.getVisitedCountries();
        cc.setVisited(country0);
        assertEquals(1, testSync.size());
    }
}
