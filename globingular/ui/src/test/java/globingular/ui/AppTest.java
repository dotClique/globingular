package globingular.ui;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.persistence.FileHandler;
import globingular.persistence.PersistenceHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Popup;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Assertions.assertFalse(countriesList.getItems().contains(no), "Clean before running tests :)");
        countryInput.setText(no.getCountryCode());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(countryAdd);
        Assertions.assertEquals("", countryInput.getText());
        Assertions.assertTrue(countriesList.getItems().contains(no));
        countryInput.setText(no.getCountryCode());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(countryDel);
        Assertions.assertFalse(countriesList.getItems().contains(no));
    }

    @Test
    public void testAddCountryVisitWithDateRange() {
        Assertions.assertFalse(countriesList.getItems().contains(jp), "Clean before running tests :)");
        countryInput.setText(jp.getShortName());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(visitsButton);

        definePopupDialogs();
        arrival.setValue(LocalDate.now());
        departure.setValue(LocalDate.now());
        clickOn(visitAdd);
        Assertions.assertTrue(countriesList.getItems().contains(jp));
    }

    @Test
    public void testAddCountryVisitWithInvalidDateRangeFails() {
        cc.removeAllVisitsToCountry(jp);
        countryInput.setText(jp.getCountryCode());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(visitsButton);

        definePopupDialogs();
        arrival.getEditor().setText("12.");
        departure.setValue(LocalDate.now());
        clickOn(visitAdd);
        Assertions.assertFalse(countriesList.getItems().contains(jp));
    }

    @Test
    public void testAddCountryVisitUsingText() {
        assertNotNull(cc);
        Country dk = cc.getWorld().getCountryFromName("Denmark");
        cc.removeAllVisitsToCountry(dk);
        countryInput.setText(dk.getCountryCode());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(visitsButton);

        definePopupDialogs();
        arrival.getEditor().setText("12.12.2020");
        departure.getEditor().setText("12.12.2020");
        clickOn(visitAdd);
        Assertions.assertTrue(countriesList.getItems().contains(dk));
    }

    @Test
    public void testUserInputValidation() {
        var userInput = (TextField) parent.lookup("#userInput");
        doubleClickOn(userInput, MouseButton.PRIMARY);
        userInput.setText("lars");
        assertFalse(userInput.getPseudoClassStates().contains(AppController.INVALID));
        userInput.setText("Local user");
        assertFalse(userInput.getPseudoClassStates().contains(AppController.INVALID));
        userInput.setText("he i");
        assertTrue(userInput.getPseudoClassStates().contains(AppController.INVALID));
        userInput.setText("");
        type(KeyCode.ENTER);
        assertEquals("Local user", userInput.getText());

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

    private void definePopupDialogs() {
        Popup popup = controller.getVisitsPopup();
        Parent popupRoot = (Parent) popup.getContent().get(0);
        visitsList = (ListView<Visit>) popupRoot.lookup("#visitsPopupListView");
        arrival = (DatePicker) popupRoot.lookup("#arrivalDatePicker");
        departure = (DatePicker) popupRoot.lookup("#departureDatePicker");
        visitAdd = (Button) popupRoot.lookup("#addVisitButton");
        visitDel = (Button) popupRoot.lookup("#removeVisitButton");
    }

    @Test
    public void testRemoveVisitFromPopup() {
        assertNotNull(cc);
        Country is = cc.getWorld().getCountryFromName("Israel");
        cc.removeAllVisitsToCountry(is);
        countryInput.setText(is.getCountryCode());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(visitsButton);

        definePopupDialogs();
        var date = LocalDate.now();
        arrival.setValue(date);
        departure.setValue(date);
        visitsList.getItems().clear();
        clickOn(visitAdd);
        assertEquals(date, visitsList.getItems().get(0).getArrival());
        arrival.setValue(date);
        departure.setValue(date);
        clickOn(visitDel);
        assertTrue(visitsList.getItems().isEmpty());
    }

    @Test
    public void testRemoveVisitsFromPopupWithList() {
        assertNotNull(cc);
        Country is = cc.getWorld().getCountryFromName("Israel");
        cc.removeAllVisitsToCountry(is);
        countryInput.setText(is.getCountryCode());
        scrollPane.setVvalue(scrollPane.getVmax());
        clickOn(visitsButton);

        definePopupDialogs();
        var date = LocalDate.now();
        arrival.setValue(date);
        departure.setValue(date);
        visitsList.getItems().clear();
        clickOn(visitAdd);
        assertEquals(date, visitsList.getItems().get(0).getArrival());
        clickOn(visitsList);
        type(KeyCode.ENTER);
        sleep(1000);
        clickOn(visitDel);
        sleep(1000);
        assertTrue(visitsList.getItems().isEmpty());
    }
}
