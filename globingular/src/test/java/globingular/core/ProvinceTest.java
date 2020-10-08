package globingular.core;

import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class ProvinceTest extends ApplicationTest {

    private Province province;

    @Override
    public void start(final Stage stage) {
        province = new Province("AU-ACT", "Canberra", "Australian Capital Territory", 426709);

    }

    @Test
    public void testGetProvinceCode() {
        String actual = province.getProvinceCode();
        Assertions.assertEquals("AU-ACT", actual);

    }

    @Test
    public void testGetName() {
        String actual = province.getName();
        Assertions.assertEquals("Australian Capital Territory", actual);
    }

    @Test
    public void testGetCapital() {
        String actual = province.getCapital();
        Assertions.assertEquals("Canberra", actual);

    }

    @Test
    public void testGetPopulation() {
        Long actual = province.getPopulation();
        Assertions.assertEquals(426709, actual);

    }
}
