package globingular.ui;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.CountryStatistics;
import globingular.core.World;
import globingular.persistence.PersistenceHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Worker;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

/**
 * <p>The AppController class handles the interaction between the FXML-file and
 * the back-end logic of the app. It makes use of several classes in core,
 * and thus is able to display the data in the JSON-file in the GUI.</p>
 * 
 * <p>The AppController has several methods, e.g.:
 * <ul>
 * <li>initializing items in the GUI correctly</li>
 * <li>updating what's viewed on the screen</li>
 * <li>adding and deleting countries from the visited countries list</li>
 * </ul>
 * </p>
 */

public class AppController implements Initializable {

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
     * Name of the Javascript-const in the WebView set to the map SVG-element.
     */
    private static final String MAP_ELEMENT_NAME = "countryMap";

    /**
     * ID of the map SVG element in the WebView.
     */
    private static final String MAP_ELEMENT_ID = "Earth";

    /**
     * The HTML-attribute declaring that this element is a visited country.
     * Also serves as the value of the attribute, if set.
     */
    private static final String MAP_VISITED_COUNTRY_ATTRIBUTE = "visited";

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
     * The WebView containing the world map.
     */
    @FXML
    private WebView webView;

    /**
     * A HBox containing the entire statistics gridpane.
     */
    @FXML
    private HBox statistics;

    /**
     * A GridPane where statistics will be added.
     */
    @FXML
    private GridPane statisticsGrid;

    /**
     * The WebEngine for the world map.
     */
    private WebEngine webEngine;
    /**
     * Responsible for persisting state across runs.
     */
    private final PersistenceHandler persistence;
    /**
     * Manager of which countries have been visited.
     */
    private final CountryCollector countryCollector;
    /**
     * Manager of which countries exist.
     */
    private final World world;
    /**
     * Helper field containing whichever Country the input field currently resolves to, if any.
     */
    private Country inputCountry = null;
    /**
     * The HTML Document containing the world map.
     */
    private Document document;

    /**
     * Manager of statistics about countries the user has visited.
     */
    private CountryStatistics countryStatistics;

    /**
     * Font size for titles in statistics tab.
     */
    private static final int TITLE_FONT_SIZE = 26;

    /**
     * Padding for labels in gridpane.
     */
    private static final int TEXT_LABEL_PADDING = 10;

    /**
     * Initialize fields which do not require FXML to be loaded.
     */
    public AppController() {
        // Create a persistenceHandler
        persistence = new PersistenceHandler();
        // Use it to retrieve CountryCollector from file
        countryCollector = persistence.loadCountryCollector();
        // And register it for autosaving
        persistence.setAutosave(PersistenceHandler.DEFAULT_USERNAME, countryCollector);

        // Initialize a countryStatistics
        countryStatistics = new CountryStatistics(countryCollector);

        // Get world-instance
        world = countryCollector.getWorld();
    }

    /**
     * Initialize the controller on load.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.webEngine = webView.getEngine();

        countryCollector.addListener(event -> {
            if (event.wasAdded()) {
                this.setVisitedOnMap(event.getElement().getCountry());
            } else if (event.wasRemoved() && !countryCollector.isVisited(event.getElement().getCountry())) {
                this.setNotVisitedOnMap(event.getElement().getCountry());
            }
            updateStatistics();
        });

        initializeCountriesList();

        countryInput.textProperty().addListener(e -> onInputChange());
        root.getStylesheets().add(getClass().getResource("/fxml-css/App.css").toExternalForm());

        webEngine.load(getClass().getResource("/html/world.html").toExternalForm());

        // Operations to perform when SVG has loaded
        webEngine.getLoadWorker().stateProperty().addListener(((observableValue, state, t1) -> {
            if (t1 == Worker.State.SUCCEEDED) {
                this.document = webEngine.getDocument();
                webEngine.executeScript("const " + MAP_ELEMENT_NAME + " = document.getElementById('" + MAP_ELEMENT_ID
                        + "').getSVGDocument();");
                registerClickListenersOnExistingCountries();
                setVisitedOnMapAll(countryCollector.getVisitedCountries());
            }
        }));
        updateStatistics();
        configureAutoComplete();
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
            Country countryByCode = world.getCountryFromCode(input.toUpperCase());
            Country countryByName = world.getCountryFromName(input.toLowerCase());
            if (countryByCode != null) {
                countryCollector.registerVisit(countryByCode);
                countryInput.clear();
            } else if (countryByName != null) {
                countryCollector.registerVisit(countryByName);
                countryInput.clear();
            } else {
                countryInput.pseudoClassStateChanged(INVALID, true);
            }

        }
    }

    /**
     * Register a click listener on every SVG element representing an entire Country registered in this world.
     */
    private void registerClickListenersOnExistingCountries() {
        for (Country country : world.getCountries()) {
            try {
                final Element countryElement = getCountryMapElement(country);
                if (countryElement == null) {
                    System.out.println(
                            "Given country does not exist on map as id: " + country.getCountryCode());
                } else {
                    ((EventTarget) countryElement).addEventListener("click",
                            e -> {
                                if (countryCollector.isVisited(country)) {
                                    countryCollector.removeAllVisitsToCountry(country);
                                } else {
                                    countryCollector.registerVisit(country);
                                }
                            },
                            true);
                }
            } catch (ClassCastException e) {
                System.out.println("Given country does not exist on map: " + country);
            }
        }
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
            Country countryByCode = world.getCountryFromCode(input.toUpperCase());
            Country countryByName = world.getCountryFromName(input.toLowerCase());

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
            countryCollector.removeAllVisitsToCountry(inputCountry);
        } else {
            // Array conversion necessary to prevent the removal of items from foreach-target
            for (Country country
                    : countriesList.getSelectionModel().getSelectedItems().toArray(Country[]::new)) {
                countryCollector.removeAllVisitsToCountry(country);
            }
        }
        countriesList.getSelectionModel().clearSelection();
    }

    /**
     * Set the attribute "visited" for the given Country's map representation, allowing custom css styling.
     *
     * @param country Target country
     */
    private void setVisitedOnMap(final Country country) {
        // Abort if map hasn't fully loaded
        if (document == null) {
            return;
        }

        getCountryMapElement(country).setAttribute(MAP_VISITED_COUNTRY_ATTRIBUTE, MAP_VISITED_COUNTRY_ATTRIBUTE);
    }

    /**
     * Unset the attribute "visited" for the given Country's map representation, removing custom css styling.
     *
     * @param country Target country
     */
    private void setNotVisitedOnMap(final Country country) {
        // Abort if map hasn't fully loaded
        if (document == null) {
            return;
        }

        getCountryMapElement(country).removeAttribute(MAP_VISITED_COUNTRY_ATTRIBUTE);
    }

    /**
     * Change displayed color on the world map for the given countries.
     *
     * @param countries Target countries
     */
    private void setVisitedOnMapAll(final Collection<Country> countries) {
        for (Country country : countries) {
            setVisitedOnMap(country);
        }
    }

    /**
     * Remove the attribute "visited" for the given countries' map representations, allowing custom css styling.
     *
     * @param countries Target countries
     */
    private void setNotVisitedOnMapAll(final Collection<Country> countries) {
        for (Country country : countries) {
            setNotVisitedOnMap(country);
        }
    }

    /**
     * Update statistics view in the UI.
     */
    private void updateStatistics() {
        statisticsGrid.getChildren().clear();
        int newRowIndex = 0;

        Label title = createConfiguredLabel("Statistics");
        title.setFont(Font.font("System", TITLE_FONT_SIZE));
        statisticsGrid.add(title, 0, newRowIndex++);

        for (Map.Entry<String, String> entry : countryStatistics.getAllStatistics().entrySet()) {
            statisticsGrid.addRow(newRowIndex++, createConfiguredLabel(entry.getKey() + ": "),
                createConfiguredLabel(entry.getValue()));
        }
    }

    /**
     * Creates a configured label with padding.
     * @param text a string which you want to display as a label
     * @return a label with the desired text
     */
    private Label createConfiguredLabel(final String text) {
        Label label = new Label(text);
        label.setPadding(new Insets(TEXT_LABEL_PADDING));
        return label;
    }

    /**
     * Configures the autocomplete, by retrieving necessary data and calling bindAutoCompletion on countryInput.
     */
    private void configureAutoComplete() {
        List<String> countryNamesAndCodes = world.getCountries()
            .stream()
            .map(Country::getShortName)
            .collect(Collectors.toList());
        countryNamesAndCodes.addAll(world.getCountries()
            .stream()
            .map(Country::getCountryCode)
            .collect(Collectors.toList()));

        TextFields.bindAutoCompletion(countryInput, countryNamesAndCodes);
    }

    /**
     * Unset the attribute "visited" for the given countries' map representations, removing custom css styling.
     */
    private void initializeCountriesList() {
        countriesList.itemsProperty().set(createSortedVisitedCountriesList(countryCollector));
        countriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        countriesList.setCellFactory(countryListView -> new ListCell<>() {
            @Override
            protected void updateItem(final Country item, final boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : item.getShortName());
            }
        });
    }

    /**
     * Find the element on the map representing the given Country.
     *
     * @param country Country to find the map element of
     * @return Country element in map
     * @throws ClassCastException If element does not exist
     */
    private Element getCountryMapElement(final Country country) {
        return (Element) webEngine
                .executeScript(MAP_ELEMENT_NAME + ".getElementById('" + country.getCountryCode() + "')");
    }

    /**
     * Create a readonly sorted-list view for the countries visited.
     *
     * @param countryCollector The countryCollector to synchronize with
     * @return A new sorted-list view for the countries visited
     */
    private static ObservableList<Country> createSortedVisitedCountriesList(final CountryCollector countryCollector) {
        ObservableList<Country> backing = FXCollections.observableArrayList();
        SortedList<Country> sorted = new SortedList<>(backing, Comparator.comparing(Country::getShortName));
        backing.addAll(countryCollector.getVisitedCountries());
        countryCollector.addListener(event -> {
            if (event.wasAdded()) {
                backing.add(event.getElement().getCountry());
            } else if (event.wasRemoved()) {
                backing.remove(event.getElement().getCountry());
            }
        });
        return sorted;
    }
}
