package globingular.core;

import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CountryTest extends ApplicationTest {

    private Country country;

    @Override
    public void start(final Stage stage) {
       country = mock(Country.class);
       when(country.getCountryCode()).thenReturn("NO");
       when(country.getName()).thenReturn("Norway");
       when(country.getLongname()).thenReturn("The Kingdom of Norway");
       when(country.getSovereignty()).thenReturn("UN");
       when(country.getRegion()).thenReturn("EU");
       when(country.getPopulation()).thenReturn((long) 5367580);
    }

    @Test
    public void testGetCountryCode() {
        String result = country.getCountryCode();
        Assertions.assertEquals("NO", result);

    }

    @Test
    public void testName() {
        String result = country.getName();
        assertThat(result).isEqualTo("Norway");

    }

    @Test
    public void testLongName() {
        String result = country.getLongname();
        Assertions.assertEquals("The Kingdom of Norway", result);

    }

    @Test
    public void testSovereignty() {
        String result = country.getSovereignty();
        Assertions.assertEquals("UN", result);

    }

    @Test
    public void testGetRegion() {
        String result = country.getRegion();
        Assertions.assertEquals("EU", result);

    }

    @Test
    public void testGetPopulation() {
        Long result = country.getPopulation();
        Assertions.assertEquals( 5367580, result);

    }
}
