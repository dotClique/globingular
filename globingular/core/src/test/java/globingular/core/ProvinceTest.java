package globingular.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProvinceTest  {

    private Province province;

    @BeforeEach
    public void start() {
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
