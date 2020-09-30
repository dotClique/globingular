package globingular.ui;

import globingular.core.Country;
import globingular.core.CountryCollector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppTest extends ApplicationTest {

    private Parent parent;
    private AppController controller;

    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/App.fxml"));
        parent = fxmlLoader.load();
        controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @Test
    public void testController() {
        final ListView<Country> countriesList = (ListView<Country>) parent.lookup(
                "#countriesList");
        final CountryCollector cc = controller.countryCollector;
        assertNotNull(cc);
        assertNotNull(cc.getWorld());
        final TextField countryInput = (TextField) parent.lookup("#countryInput");
        final Button countryAdd = (Button) parent.lookup("#countryAdd");
        final Button countryDel = (Button) parent.lookup("#countryDel");
        Country au = cc.getWorld().getCountryFromCode("AU");

        if (countriesList.getItems().contains(au)) {
            countryInput.setText(au.getCountryCode());
            clickOn(countryDel);
            Assertions.assertFalse(countriesList.getItems().contains(au));
        }
        Assertions.assertFalse(countriesList.getItems().contains(au));
        countryInput.setText(au.getCountryCode());
        clickOn(countryAdd);
        Assertions.assertEquals("", countryInput.getText());
        Assertions.assertTrue(countriesList.getItems().contains(au));
        countryInput.setText(au.getCountryCode());
        clickOn(countryDel);
        Assertions.assertFalse(countriesList.getItems().contains(au));
    }
}
