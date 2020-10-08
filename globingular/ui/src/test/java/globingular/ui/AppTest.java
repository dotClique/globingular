package globingular.ui;

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
        final ListView<String> countriesList = (ListView<String>) parent.lookup("#countriesList");
        final TextField countryInput = (TextField) parent.lookup("#countryInput");
        final Button countryAdd = (Button) parent.lookup("#countryAdd");
        final Button countryDel = (Button) parent.lookup("#countryDel");

        if (countriesList.getItems().contains("AU")) {
            countryInput.setText("AU");
            clickOn(countryDel);
            Assertions.assertFalse(countriesList.getItems().contains("AU"));
        }
        Assertions.assertFalse(countriesList.getItems().contains("AU"));
        countryInput.setText("AU");
        clickOn(countryAdd);
        Assertions.assertEquals("", countryInput.getText());
        Assertions.assertTrue(countriesList.getItems().contains("AU"));
        countryInput.setText("AU");
        clickOn(countryDel);
        Assertions.assertFalse(countriesList.getItems().contains("AU"));
    }
}
