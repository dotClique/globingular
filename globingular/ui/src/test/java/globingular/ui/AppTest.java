package globingular.ui;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.persistence.FileHandler;
import globingular.persistence.PersistenceHandler;
import javafx.scene.control.ScrollPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.LocalDate;

/**
 * <p>
 * The AppTest is a test class that allows for testing of the functionality in
 * AppController.
 * </p>
 */
public class AppTest extends ApplicationTest {

    private Parent parent;
    private AppController controller;

    private ListView<Country> countriesList;
    private CountryCollector cc;
    private TextField countryInput;
    private Button countryAdd;
    private Button countryDel;
    private Button visitsButton;
    private ScrollPane scrollPane;

    private ListView<Visit> visitsList;
    private DatePicker arrival;
    private DatePicker departure;
    private Button visitAdd;
    private Button visitDel;

    private Country au, jp, no;

    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/App.fxml"));
        parent = fxmlLoader.load();
        controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @BeforeEach
    public void beforeEach() throws IllegalArgumentException, IOException {
        defineGeneralDialogs();

        assertNotNull(cc);
        assertNotNull(cc.getWorld());
        au = cc.getWorld().getCountryFromCode("AU");
        jp = cc.getWorld().getCountryFromCode("JP");
        no = cc.getWorld().getCountryFromCode("NO");

        // Reset fileStorage
        FileHandler.saveCountryCollector(new PersistenceHandler(), null, null);
    }

    @Test
    public void testAddCountryVisit() {
        Assertions.assertFalse(countriesList.getItems().contains(au), "Clean before running tests :)");
        countryInput.setText(au.getCountryCode());
        // Scroll in case the window is to low to display button
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(countryAdd);
        Assertions.assertEquals("", countryInput.getText());
        Assertions.assertTrue(countriesList.getItems().contains(au));
    }

    @Test
    public void testAddAndRemoveCountryVisit() {
        Assertions.assertFalse(countriesList.getItems().contains(au), "Clean before running tests :)");
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

    private void defineGeneralDialogs() {
        countriesList = (ListView<Country>) parent.lookup("#countriesList");
        cc = controller.getCountryCollector();
        countryInput = (TextField) parent.lookup("#countryInput");
        countryAdd = (Button) parent.lookup("#countryAdd");
        countryDel = (Button) parent.lookup("#countryDel");
        visitsButton = (Button) parent.lookup("#visitsButton");
        scrollPane = (ScrollPane) parent.lookup("#mapPageScroll");
    }
}
