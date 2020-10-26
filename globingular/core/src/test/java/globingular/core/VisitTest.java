package globingular.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class VisitTest {
    static Country country0;
    static LocalDateTime time0;

    @BeforeAll
    public static void start() {
        country0 = new Country("_C0", "_country0");
        time0 = LocalDateTime.now();
    }

    @Test
    public void testConstructor() {
        Visit v = new Visit(country0, time0);
        assertEquals(country0, v.getCountry());
        assertEquals(time0, v.getArrival());
    }
}
