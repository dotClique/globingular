package globingular.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import globingular.core.Country;
import globingular.core.CountryCollector;
import javafx.scene.control.ScrollPane;
import org.junit.jupiter.api.Assertions;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppIT extends ApplicationTest {

    private Parent parent;
    private AppController controller;

    @Override
    public void start(final Stage primaryStage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AppIT.fxml"));
        parent = loader.load();
        this.controller = loader.getController();
        primaryStage.setScene(new Scene(parent, 1300, 600));
        primaryStage.show();
    }

    @BeforeEach
    public void setupItems() throws URISyntaxException {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("/json/sampleCollector.json"))) {
            String port = "8081";
            URI baseUri = new URI("http://localhost:" + port + "/globingular/");
            System.out.println("Base URI: " + baseUri);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testController_initial() {
        assertNotNull(this.controller);
    }

    @Test
    public void testController() {
        final ListView<Country> countriesList = (ListView<Country>) parent.lookup(
                "#countriesList");
        final CountryCollector cc = controller.getCountryCollector();
        assertNotNull(cc);
        assertNotNull(cc.getWorld());
        final TextField countryInput = (TextField) parent.lookup("#countryInput");
        final Button countryAdd = (Button) parent.lookup("#countryAdd");
        final Button countryDel = (Button) parent.lookup("#countryDel");
        final ScrollPane scrollPane = (ScrollPane) parent.lookup("#mapPageScroll");
        Country au = cc.getWorld().getCountryFromCode("AU");

        if (countriesList.getItems().contains(au)) {
            countryInput.setText(au.getCountryCode());
            scrollPane.setVvalue(scrollPane.getVmax());
            clickOn(countryDel);
            Assertions.assertFalse(countriesList.getItems().contains(au));
        }
        Assertions.assertFalse(countriesList.getItems().contains(au));
        countryInput.setText(au.getCountryCode());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(countryAdd);
        Assertions.assertEquals("", countryInput.getText());
        Assertions.assertTrue(countriesList.getItems().contains(au));
        countryInput.setText(au.getCountryCode());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(countryDel);
        Assertions.assertFalse(countriesList.getItems().contains(au));

    }

    @Test
    public void testServerIntegration() {
        final TextField userInput = (TextField) parent.lookup("#userInput");
        final Button changeUser = (Button) parent.lookup("#changeUser");

        clickOn(changeUser);
        userInput.setText("lovelace");
        clickOn(changeUser);
        
        Assertions.assertEquals("lovelace", userInput.getText());
    }

    @Test
    public void checkCountriesUnclickedWhenChangeUser() {
        final ListView<Country> countriesList = (ListView<Country>) parent.lookup(
                "#countriesList");
        final ScrollPane scrollPane = (ScrollPane) parent.lookup("#mapPageScroll");
        final CountryCollector cc = controller.getCountryCollector();
        Country jp = cc.getWorld().getCountryFromCode("JP");
        final TextField countryInput = (TextField) parent.lookup("#countryInput");
        final TextField userInput = (TextField) parent.lookup("#userInput");
        countryInput.setText("Japan");
        final Button countryAdd = (Button) parent.lookup("#countryAdd");
        final Button changeUser = (Button) parent.lookup("#changeUser");
        clickOn(countryAdd);
        scrollPane.setVvalue(scrollPane.getVmax());

        clickOn(changeUser);
        userInput.setText("tesla");
        clickOn(changeUser);
        Assertions.assertFalse(countriesList.getItems().contains(jp));
    }
}
