package globingular.ui;

import globingular.core.CountryCollector;
import globingular.json.PersistenceHandler;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private PersistenceHandler persistence;
    public CountryCollector countryCollector;
    private final Document document = new CreateDocument().createDocument();
    private CountryCollector.Country inputCountry = null;

    public static PseudoClass INVALID = new PseudoClass() {
        @Override
        public String getPseudoClassName() {
            return "invalid";
        }
    };

    public static PseudoClass BLANK = new PseudoClass() {
        @Override
        public String getPseudoClassName() {
            return "blank";
        }
    };

    @FXML
    Parent root;

    @FXML
    ListView<CountryCollector.Country> countriesList;

    @FXML
    TextField countryInput;

    @FXML
    Button countryAdd;

    @FXML
    Button countryDel;

    @FXML
    ImageView imgView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        persistence = new PersistenceHandler();
        countryCollector = persistence.loadState();
        setColorAll(countryCollector.getVisitedCountries(), Colors.COUNTRY_VISITED);

        countryCollector.visitedCountriesProperty().addListener((SetChangeListener<? super CountryCollector.Country>) e -> {
            if (e.wasAdded()) {
                setColor(e.getElementAdded(), Colors.COUNTRY_VISITED);
            } else {
                setColor(e.getElementRemoved(), Colors.COUNTRY_NOT_VISITED);
            }
        });

        countriesList.itemsProperty().set(countryCollector.getVisitedCountriesSorted());
        countriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        countriesList.setCellFactory(countryListView -> new ListCell<>() {
            @Override
            protected void updateItem(CountryCollector.Country item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : item.getName());
            }
        });

        countryInput.textProperty().addListener(e -> onInputChange());
        root.getStylesheets().add(getClass().getResource("/css/App.css").toExternalForm());

        updateMap();
    }

    @FXML
    void onCountryAdd() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            CountryCollector.Country countryByCode = countryCollector.getCountryFromCode(input);
            CountryCollector.Country countryByName = countryCollector.getCountryFromName(input);
            if (countryByCode != null) {
                countryCollector.setVisited(countryByCode);
                countryInput.clear();
            } else if (countryByName != null) {
                countryCollector.setVisited(countryByName);
                countryInput.clear();
            } else {
                countryInput.pseudoClassStateChanged(INVALID, true);
            }

        }
        persistence.saveState(countryCollector);
    }

    private void onInputChange() {
        String input = countryInput.getText();
        if (input.isBlank()) {
            countryInput.pseudoClassStateChanged(BLANK, true);
            countryInput.pseudoClassStateChanged(INVALID, false);
        } else {
            CountryCollector.Country countryByCode = countryCollector.getCountryFromCode(input);
            CountryCollector.Country countryByName = countryCollector.getCountryFromName(input);

            if (countryByCode != null) {
                inputCountry = countryByCode;
                countryInput.pseudoClassStateChanged(INVALID, false);
            } else if (countryByName != null) {
                inputCountry = countryByName;
                countryInput.pseudoClassStateChanged(INVALID, false);
            } else {
                inputCountry = null;
                countryInput.pseudoClassStateChanged(INVALID, true);
            }
            countryInput.pseudoClassStateChanged(BLANK, false);
        }
    }

    @FXML
    void onCountryDel() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            if (!countryInput.getPseudoClassStates().contains(INVALID))
            countryInput.clear();
            countryCollector.removeVisited(inputCountry);
        } else {
            // Array conversion necessary to prevent the removal of items from foreach-target
            for (CountryCollector.Country country :
                    countriesList.getSelectionModel().getSelectedItems().toArray(CountryCollector.Country[]::new)) {
                countryCollector.removeVisited(country);
            }
        }
        countriesList.getSelectionModel().clearSelection();
        persistence.saveState(countryCollector);
    }

    private void setColor(CountryCollector.Country country, Colors color) {
        Element c = document.getElementById(country.getCountryCode());

        if (c == null) {
            return;
        }

        c.setAttribute("style", "fill: " + color.hex);
        updateMap();
    }

    private void setColorAll(Collection<CountryCollector.Country> countries, Colors color) {
        for (CountryCollector.Country country : countries) {
            setColor(country, color);
        }
    }

    private void updateMap() {
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        TranscoderInput transcoderIn = new TranscoderInput(document);
        try {
            transcoder.transcode(transcoderIn, null);
            Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
            imgView.setImage(img);

        } catch (TranscoderException e) {
            e.printStackTrace();
        }
    }
}
