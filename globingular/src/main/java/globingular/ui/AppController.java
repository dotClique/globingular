package globingular.ui;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.World;
import globingular.json.PersistenceHandler;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
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

    /**
     * Responsible for persisting state across runs.
     */
    private PersistenceHandler persistence;
    /**
     * Manager of which countries have been visited.
     */
    private final CountryCollector countryCollector;
    /**
     * Manager of which countries exist.
     */
    private final World world;
    /**
     * The SVG document containing the world map.
     */
    private final Document document = new CreateDocument().createDocument();
    /**
     * Helper field containing whichever Country the input field currently resolves to, if any.
     */
    private Country inputCountry = null;

    /**
     * Pseudoclass designating that an form element has invalid input.
     */
    public static final PseudoClass INVALID = new PseudoClass() {
        @Override
        public String getPseudoClassName() {
            return "invalid";
        }
    };
    /**
     * Pseudoclass designating that an form element has blank input.
     */
    public static final PseudoClass BLANK = new PseudoClass() {
        @Override
        public String getPseudoClassName() {
            return "blank";
        }
    };
    /**
     * The root of the FXML document.
     */
    @FXML
    private Parent root;

    /**
     * The list of countries visible in the interface.
     */
    @FXML
    private ListView<Country> countriesList;

    /**
     * The input for country-shortnames or -codes visible in the interface.
     */
    @FXML
    private TextField countryInput;

    /**
     * The button for marking the inputted country-code as visited.
     */
    @FXML
    private Button countryAdd;

    /**
     * The button for marking the inputted country-code as not visited.
     */
    @FXML
    private Button countryDel;

    /**
     * The visible image view holding the map-svg converted to JavaFX-legible format.
     */
    @FXML
    private ImageView imgView;

    /**
     * Initialize field which do not require FXML to be loaded.
     */
    public AppController() {
        persistence = new PersistenceHandler();
        countryCollector = persistence.loadMapCountryCollector();
        world = countryCollector.getWorld();
    }

    /**
     * Initialize the controller on load.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        setColorAll(countryCollector.getVisitedCountries(), Colors.COUNTRY_VISITED);

        countryCollector.visitedCountriesProperty()
                        .addListener((SetChangeListener<? super Country>) e -> {
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
            protected void updateItem(final Country item, final boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : item.getShortName());
            }
        });

        countryInput.textProperty().addListener(e -> onInputChange());
        root.getStylesheets().add(getClass().getResource("/css/App.css").toExternalForm());

        updateMap();
    }

    /**
     * Get the active CountryCollector for the world map.
     *
     * @return The active CountryCollector for the world map
     */
    public CountryCollector getCountryCollector() {
        return countryCollector;
    }

    /**
     * Add the inputted country-code to list of visited countries.
     */
    @FXML
    void onCountryAdd() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            Country countryByCode = world.getCountryFromCode(input);
            Country countryByName = world.getCountryFromName(input);
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

    /**
     * Evaluate new state after a change in the country input element.
     */
    private void onInputChange() {
        String input = countryInput.getText();
        if (input.isBlank()) {
            countryInput.pseudoClassStateChanged(BLANK, true);
            countryInput.pseudoClassStateChanged(INVALID, false);
        } else {
            Country countryByCode = world.getCountryFromCode(input);
            Country countryByName = world.getCountryFromName(input);

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

    /**
     * Remove the inputted country-code from list of visited countries.
     */
    @FXML
    void onCountryDel() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            if (!countryInput.getPseudoClassStates().contains(INVALID)) {
                countryInput.clear();
            }
            countryCollector.removeVisited(inputCountry);
        } else {
            // Array conversion necessary to prevent the removal of items from foreach-target
            for (Country country
                    : countriesList.getSelectionModel().getSelectedItems().toArray(Country[]::new)) {
                countryCollector.removeVisited(country);
            }
        }
        countriesList.getSelectionModel().clearSelection();
        persistence.saveState(countryCollector);
    }

    /**
     * Change displayed color on the world map for the given country.
     *
     * @param country Target country
     * @param color   Target color
     */
    private void setColor(final Country country, final Colors color) {
        Element c = document.getElementById(country.getCountryCode());

        if (c == null) {
            return;
        }

        c.setAttribute("style", "fill: " + color.getHex());
        updateMap();
    }

    /**
     * Change displayed color on the world map for the given countries.
     *
     * @param countries Target countries
     * @param color     Target color
     */
    private void setColorAll(final Collection<Country> countries, final Colors color) {
        for (Country country : countries) {
            setColor(country, color);
        }
    }

    /**
     * Change the world map to reflect updated state, e.g. country colors.
     */
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
