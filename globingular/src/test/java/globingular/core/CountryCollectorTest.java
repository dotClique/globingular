package globingular.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.stage.Stage;

public class CountryCollectorTest extends ApplicationTest {

    private CountryCollector cc;

    @Override
    public void start(final Stage stage) {
        cc = new CountryCollector("SE", "DK");
    }

    @Test
    public void testToString() {
        CountryCollector cc2 = new CountryCollector("SE");
        assertEquals("[SE]", cc2.toString());
    }

    @Test
    public void testConstructor() {
        assertEquals(2, cc.numberVisited());
        assertTrue(cc.hasVisited("SE"));
        assertTrue(cc.hasVisited("DK"));
        assertFalse(cc.hasVisited("ES"));
    }

    @Test
    public void testEmptyConstructor() {
        cc = new CountryCollector();
        assertEquals(0, cc.numberVisited());
        assertEquals("[]", cc.toString());
    }

    @Test
    public void testSetVisited() {
        assertFalse(cc.hasVisited("ES"));
        cc.setVisited("ES");
        assertTrue(cc.hasVisited("ES"));
    }

    @Test
    public void testRemoveVisited() {
        assertTrue(cc.hasVisited("SE"));
        cc.removeVisited("SE");
        assertFalse(cc.hasVisited("SE"));
    }

    @Test
    public void testHasVisited() {
        assertFalse(cc.hasVisited("JP"));
        assertTrue(cc.hasVisited("SE"));
    }
}
