package globingular.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

public class VisitTest {
    static Country country0;
    static LocalDate date0, date1;

    @BeforeAll
    public static void start() {
        country0 = new Country("_C0", "_country0");
        date0 = LocalDate.now();
        date1 = LocalDate.now();
    }

    @Test
    public void testConstructor() {
        Visit v = new Visit(country0, date0, date1);
        assertEquals(country0, v.getCountry());
        assertEquals(date0, v.getArrival());
        assertEquals(date1, v.getDeparture());
    }
}
