package globingular.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import globingular.core.CountryCollector;
import globingular.json.PersistenceHandler;

public class AppController implements Initializable {

    private PersistenceHandler persistence;
    private CountryCollector countryCollector;

    @FXML
    ListView countriesList;

    @FXML
    TextField countryInput;

    @FXML
    Button countryAdd;

    @FXML
    Button countryDel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        persistence = new PersistenceHandler();
        countryCollector = persistence.loadState();
        updateView();
    }

    private void updateView() {
        countriesList.getItems().clear();
        countriesList.getItems().addAll(List.of(countryCollector.getVisitedCountries()));
    }

    @FXML
    void onCountryAdd() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            countryCollector.setVisited(input);
            countryInput.clear();
        }
        updateView();
        persistence.saveState(countryCollector);
    }

    @FXML
    void onCountryDel() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            countryCollector.removeVisited(input);
            countryInput.clear();
        }
        updateView();
        persistence.saveState(countryCollector);
    }
}
