package globingular.ui;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.CountryStatistics;
import globingular.core.Listener;
import globingular.core.Visit;
import globingular.core.GlobingularModule;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
     * Username representing the local user.
     * Contains a disallowed username-character so as to distinguish it from normal usernames.
     */
    private static final String LOCAL_USER = "Local user";

    /**
     * The ListView which shows visits in the visits-popup.
     */
    @FXML
    private ListView<Visit> visitsPopupListView;
    /**
     * Root node of the visits-popup.
     */
    @FXML
    private Parent visitsPopupRoot;
    /**
     * The label in the visits-popup showing the name of the {@link Country}.
     */
    @FXML
    private Label visitsPopupCountryNameLabel;
    /**
     * Date-picker for the arrival-date of a {@link Visit} in the visits-popup.
     */
    @FXML
    private DatePicker arrivalDatePicker;
    /**
     * Date-picker for the departure-date of a {@link Visit} in the visits-popup.
     */
    @FXML
    private DatePicker departureDatePicker;
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
     * TextField used to display and change current user.
     */
    @FXML
    private TextField userInput;
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
    private final PersistenceHandler persistence = new PersistenceHandler();
    /**
     * Manager of which countries have been visited.
     */
    private CountryCollector countryCollector;
    /**
     * Manager of which countries exist.
     */
    private World world;
    /**
     * Helper field containing whichever Country the input field currently resolves to, if any.
     */
    private Country inputCountry = null;
    /**
     * The Country whose visits were most recently displayed (which may be currently).
     */
    private Country popupCountry;
    /**
     * The HTML Document containing the world map.
     */
    private Document document;
    /**
     * The current user.
     */
    private String currentUser;
    /**
     * The intermediary for accessing stored data.
     */
    private GlobingularDataAccess dataAccess;
    /**
     * Manager of statistics about countries the user has visited.
     */
    private CountryStatistics countryStatistics;
    /**
     * Binding for autocompletion for {@link #countryInput}.
     */
    private AutoCompletionBinding<String> countryInputAutoCompletionBinding;
    /**
     * Whether the FXML has been fully loaded.
     */
    private boolean initialized = false;

    /**
     * Font size for titles in statistics tab.
     */
    private static final int TITLE_FONT_SIZE = 26;

    /**
     * Padding for labels in gridpane.
     */
    private static final int TEXT_LABEL_PADDING = 10;

    /**
     * The visits-popup, a semi-window which can be hidden and shown.
     */
    private final Popup visitsPopup = new Popup();
    /**
     * Listener responsible for opening the visits-popup of a country when said country is clicked on the map.
     */
    private final EventListener mapListenerForOpeningVisitsPopup
            = e -> popupVisits(world.getCountryFromCode(((Element) e.getCurrentTarget()).getAttribute("id")));

    /**
     * Listener responsible for updating the map and statistics when something changes in {@link #countryCollector}.
     */
    private final Listener<Visit> countryCollectorListenerForMapAndStatistics = event -> {
        if (event.wasAdded()) {
            this.setVisitedOnMap(event.getElement().getCountry());
        } else if (event.wasRemoved() && !countryCollector.isVisited(event.getElement().getCountry())) {
            this.setNotVisitedOnMap(event.getElement().getCountry());
        }
        updateStatistics();
    };

    /**
     * The listener used for saving the {@link CountryCollector} when it is updated.
     */
    private final Listener<Visit> countryCollectorListenerForSaving =
            e -> dataAccess.putCountryCollector(countryCollector);

    /**
     * Do setup that doesn't need FXML to be loaded.
     */
    public AppController() {
        // Change the user to the local user. Only does the pre-init-step of configuring app-state since the FXML hasn't
        // been initialized when the constructor is called.
        changeUser(LOCAL_USER);
    }

    /**
     * Initialize the controller on load.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        userInput.setText(LOCAL_USER);
        this.webEngine = webView.getEngine();

        postInitConfigureState(countryCollector);

        webView.setContextMenuEnabled(false);
        initializeCountriesList();
        setupVisitsPopup();

        // Re-validate the visits-popup date-pickers for every typed character,
        // instead of just on Enter-press.
        departureDatePicker.getEditor().setOnKeyTyped(e -> validateVisitDates());
        arrivalDatePicker.getEditor().setOnKeyTyped(e -> validateVisitDates());

        countryInput.textProperty().addListener(e -> onCountryInputChange());
        userInput.textProperty().addListener(e -> onUserInputChange());

        root.getStylesheets().add(getClass().getResource("/fxml-css/App.css").toExternalForm());
        visitsPopupRoot.getStylesheets().add(getClass().getResource("/fxml-css/Popup.css").toExternalForm());

        webEngine.load(getClass().getResource("/html/world.html").toExternalForm());

        // Operations to perform when SVG has loaded
        webEngine.getLoadWorker().stateProperty().addListener(((observableValue, state, t1) -> {
            if (t1 == Worker.State.SUCCEEDED) {
                this.document = webEngine.getDocument();
                webEngine.executeScript("const " + MAP_ELEMENT_NAME + " = document.getElementById('" + MAP_ELEMENT_ID
                        + "').getSVGDocument();");
                postMapLoadConfigureState(countryCollector);
            }
        }));
        initialized = true;
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
    private void onCountryAdd() {
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
                    System.err.println(
                            "Given country does not exist on map as id: " + country.getCountryCode());
                } else {
                    ((EventTarget) countryElement).addEventListener("click",
                            mapListenerForOpeningVisitsPopup,
                            true);
                }
            } catch (ClassCastException e) {
                System.err.println("Given country does not exist on map: " + country);
            }
        }
    }

    /**
     * Evaluate new state after a change in the country input element.
     */
    private void onCountryInputChange() {
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
     * Evaluate the validity of the user input element after a change in the input text.
     */
    private void onUserInputChange() {
        String input = userInput.getText();
        if (input.isBlank() || LOCAL_USER.equalsIgnoreCase(input)) {
            userInput.pseudoClassStateChanged(INVALID, false);
        } else {
            userInput.pseudoClassStateChanged(INVALID, !GlobingularModule.isUsernameValid(input.toLowerCase()));
        }
    }

    /**
     * Remove the inputted country-code from list of visited countries.
     */
    @FXML
    private void onCountryDel() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            if (!countryInput.getPseudoClassStates().contains(INVALID)) {
                countryInput.clear();
            }
            countryCollector.removeAllVisitsToCountry(inputCountry);
        } else {
            countryCollector.removeAllVisitsToCountry(countriesList.getSelectionModel().getSelectedItem());
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

        Element countryMapElement = getCountryMapElement(country);
        // Abort if the country isn't represented on the map
        if (countryMapElement == null) {
            return;
        }

        countryMapElement.setAttribute(MAP_VISITED_COUNTRY_ATTRIBUTE, MAP_VISITED_COUNTRY_ATTRIBUTE);
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

        Element countryMapElement = getCountryMapElement(country);
        // Abort if the country isn't represented on the map
        if (countryMapElement == null) {
            return;
        }

        countryMapElement.removeAttribute(MAP_VISITED_COUNTRY_ATTRIBUTE);
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

        countryInputAutoCompletionBinding = TextFields.bindAutoCompletion(countryInput, countryNamesAndCodes);
    }

    /**
     * Unset the attribute "visited" for the given countries' map representations, removing custom css styling.
     */
    private void initializeCountriesList() {
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
     * Handle the interaction when user is trying to change the active user.
     */
    @FXML
    private void onChangeUserRequested() {
        // Branch on whether userInput is editable. If so, the user has input a username into it. If not, they want to.
        if (userInput.isEditable()) {
            String text = userInput.getText();
            // Only change user if the input is valid
            if (!userInput.getPseudoClassStates().contains(INVALID)) {
                changeUser(text);
            }
            userInput.setEditable(false);
            userInput.setText(currentUser);
        } else {
            userInput.setText(currentUser);
            userInput.setEditable(true);
            userInput.requestFocus();
            userInput.selectAll();
        }
    }

    /**
     * Change the current user.
     *
     * @param toUser The user to change to. If blank or {@link #LOCAL_USER}, use unnamed local user instead.
     */
    private void changeUser(final String toUser) {
        if (!toUser.equalsIgnoreCase(currentUser)) {
            // Only reset if there was a previous user
            if (currentUser != null) {
                clearAppState();
            }
            currentUser = toUser;
            // If no user is requested, use unnamed local user
            if (toUser.isBlank() || toUser.equalsIgnoreCase(LOCAL_USER)) {
                dataAccess = new LocalGlobingularDataAccess(null, persistence);
                currentUser = LOCAL_USER;

                // Only change text if the FXML has initialized; if not, it will be done upon initialization anyway
                if (initialized) {
                    userInput.setText(LOCAL_USER);
                }
            } else {
                dataAccess = new RestGlobingularDataAccess(App.BASE_URI, currentUser.toLowerCase(), persistence);
            }
            changeAppState(dataAccess.getCountryCollector());
        }
    }

    /**
     * Reset the app-state so that a new {@link CountryCollector} can be loaded onto a clean slate.
     */
    private void clearAppState() {
        resetMapCountryElements();
        for (Listener<Visit> visitListener : countryCollector.getListeners()) {
            countryCollector.removeListener(visitListener);
        }
        countryInputAutoCompletionBinding.dispose();
        countryInput.clear();
        countriesList.setItems(null);
    }

    /**
     * Clear listeners and attributes for country map elements.
     */
    private void resetMapCountryElements() {
        for (Country country : world.getCountries()) {
            Element countryElement = getCountryMapElement(country);
            // If the Country doesn't have an element on the map, there's nothing to reset.
            if (countryElement != null) {
                countryElement.removeAttribute(MAP_VISITED_COUNTRY_ATTRIBUTE);

                ((EventTarget) countryElement).removeEventListener(
                        "click",
                        mapListenerForOpeningVisitsPopup,
                        true);
            }
        }
    }

    /**
     * Re-orient the app-state around a new CountryCollector. Called when changing user.
     * <p>
     * Three-step method. Every step must be done, in order, to completely change the state.
     * This method should be used for subsequent CountryCollector-replacement after the initial configuration.
     * If the app hasn't gone through all loading-stages, only the possible steps will be done.
     * When the {@link #initialize}-method is executed it will execute the post-init-step,
     * and schedule the post-map-load-step for when the map has loaded.
     * The scheduled steps configure the state for whichever CountryCollector
     * was last configured using the pre-init-step when they execute.
     * <p>
     * Before calling this method, {@link #clearAppState()} should be called to cleanup the previous state.
     *
     * @param toCountryCollector The new CountryCollector.
     */
    private void changeAppState(final CountryCollector toCountryCollector) {
        preInitConfigureState(toCountryCollector);

        // This part must be called after the FXML has been loaded.
        // this::initialize will run this block when it does,
        // so if it hasn't loaded yet we don't need to do anything here.
        if (initialized) {
            postInitConfigureState(toCountryCollector);

            // This part must be called after map has been loaded.
            // this::initialize schedules this block to run when it has finished loading,
            // so if it hasn't loaded yet we don't need to do anything here.
            if (webEngine.getLoadWorker().getState() == Worker.State.SUCCEEDED) {
                postMapLoadConfigureState(toCountryCollector);
            }
        }
    }

    /**
     * Do the part of configuring app state that can be done before the FXML is initialized.
     *
     * @param toCountryCollector The new CountryCollector.
     */
    private void preInitConfigureState(final CountryCollector toCountryCollector) {
        this.countryCollector = toCountryCollector;
        toCountryCollector.addListener(countryCollectorListenerForSaving);
        countryStatistics = new CountryStatistics(toCountryCollector);
        world = toCountryCollector.getWorld();
    }

    /**
     * Do the part of configuring app state that cannot be done before the FXML is initialized,
     * but does not require that the map has finished loading.
     *
     * @param toCountryCollector The new CountryCollector.
     */
    private void postInitConfigureState(final CountryCollector toCountryCollector) {
        configureAutoComplete();
        countryCollector.addListener(countryCollectorListenerForMapAndStatistics);
        updateStatistics();
        countriesList.itemsProperty().set(createSortedVisitedCountriesList(toCountryCollector));
    }

    /**
     * Do the part of configuring app state that cannot be done before the map has finished loading.
     *
     * @param toCountryCollector The new CountryCollector.
     */
    private void postMapLoadConfigureState(final CountryCollector toCountryCollector) {
        registerClickListenersOnExistingCountries();
        setVisitedOnMapAll(toCountryCollector.getVisitedCountries());
    }

    /**
     * Create a readonly sorted-list view for the countries visited.
     *
     * @param countryCollector The countryCollector to synchronize with
     * @return A new sorted-list view for the countries visited
     */
    private static ObservableList<Country> createSortedVisitedCountriesList(final CountryCollector countryCollector) {
        ObservableList<Country> backing = FXCollections.observableArrayList();
        SortedList<Country> sorted
                = new SortedList<>(backing, Comparator.comparing(Country::getShortName));
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

    /**
     * Create a readonly observable list of all visits to a certain Country, for use in bindings.
     *
     * @param targetCountryCollector The CountryCollector to get the visits from.
     * @param filterCountry The Country to filter the visits on.
     * @return A reaonly observable list of all visits to the filter Country.
     */
    private static ObservableList<Visit> createObservableVisitsToCountryList(
            final CountryCollector targetCountryCollector, final Country filterCountry) {
        ObservableList<Visit> backing = FXCollections.observableArrayList();
        SortedList<Visit> sorted = new SortedList<>(backing,
                Comparator.comparing(v -> v.getArrival() == null ? LocalDate.MIN : v.getArrival()));
        backing.addAll(targetCountryCollector.getVisitsToCountry(filterCountry));
        targetCountryCollector.addListener(event -> {
            if (event.wasAdded()) {
                if (event.getElement().getCountry() == filterCountry) {
                    backing.add(event.getElement());
                }
            } else if (event.wasRemoved()) {
                if (event.getElement().getCountry() == filterCountry) {
                    backing.remove(event.getElement());
                }
            }
        });
        return sorted;
    }

    /**
     * Called when the user clicks the field to change user.
     * @param mouseEvent The click-event.
     */
    @FXML
    private void onClickUserField(final MouseEvent mouseEvent) {
        // Attempt to change user if event was doublelick
        if (mouseEvent.getClickCount() == 2) {
            onChangeUserRequested();
        }
    }

    /**
     * Try to register a Visit using the visits-popup date-pickers and {@link Country}.
     */
    @FXML
    private void requestRegisterVisit() {
        LocalDate arrival = arrivalDatePicker.getValue();
        LocalDate departure = departureDatePicker.getValue();
        if (!arrivalDatePicker.getPseudoClassStates().contains(INVALID)
                && !departureDatePicker.getPseudoClassStates().contains(INVALID)) {
            countryCollector.registerVisit(popupCountry, arrival, departure);

            // Reset the date pickers
            arrivalDatePicker.getEditor().setText("");
            departureDatePicker.getEditor().setText("");
            validateVisitDates();
        }
    }

    /**
     * Attempt to remove one or more visit from the visits-popup.
     */
    @FXML
    private void removeVisit() {
        // Use the date-pickers if valid, list-view-selection if not
        if (!arrivalDatePicker.getPseudoClassStates().contains(INVALID)
                && !departureDatePicker.getPseudoClassStates().contains(INVALID)) {
            // Use the date-pickers to remove a Visit with those dates.
            countryCollector.removeVisit(
                    new Visit(popupCountry, arrivalDatePicker.getValue(), departureDatePicker.getValue()));
            arrivalDatePicker.getEditor().clear();
            departureDatePicker.getEditor().clear();
        } else {
            // Remove all selected visits
            for (Visit visit : visitsPopupListView.getSelectionModel().getSelectedItems().toArray(Visit[]::new)) {
                countryCollector.removeVisit(visit);
            }
        }
    }

    /**
     * Evaluate the validity of the two date input elements for Visit, possibly changin pseudoclass-states..
     */
    private void validateVisitDates() {
        // Save information for restoring field-text later
        int arrivalCaretPosition = arrivalDatePicker.getEditor().getCaretPosition();
        String arrivalText = arrivalDatePicker.getEditor().getText();

        // Try to convert the field-text to date. If it fails, mark the field as invalid.
        // If it succeeds, mark as valid.
        try {
            arrivalDatePicker.setValue(
                    arrivalDatePicker.getConverter().fromString(arrivalText));
            arrivalDatePicker.pseudoClassStateChanged(INVALID, false);
        } catch (DateTimeParseException e) {
            if (arrivalDatePicker.getValue() != null) {
                // Use stored information to restore the field-text to what the user typed
                arrivalDatePicker.setValue(null);
                arrivalDatePicker.getEditor().setText(arrivalText);
                arrivalDatePicker.getEditor().positionCaret(arrivalCaretPosition);
            }
            arrivalDatePicker.pseudoClassStateChanged(INVALID, true);
        }

        // Save information for restoring field-text later
        int departureCaretPosition = departureDatePicker.getEditor().getCaretPosition();
        String departureText = departureDatePicker.getEditor().getText();

        // Try to convert the field-text to date. If it fails, mark the field as invalid.
        // If it succeeds, mark as valid only if the departure is valid for the given arrival, or the arrival is null.
        try {
            departureDatePicker.setValue(
                    departureDatePicker.getConverter().fromString(departureText));
            departureDatePicker.pseudoClassStateChanged(INVALID,
                    arrivalDatePicker.getValue() != null && !Visit
                            .isValidDateInterval(arrivalDatePicker.getValue(), departureDatePicker.getValue()));
        } catch (DateTimeParseException e) {
            if (departureDatePicker.getValue() != null) {
                departureDatePicker.setValue(null);
                // Use stored information to restore the field-text to what the user typed
                departureDatePicker.getEditor().setText(departureText);
                departureDatePicker.getEditor().positionCaret(departureCaretPosition);
            }
            departureDatePicker.pseudoClassStateChanged(INVALID, true);
        }

        // Mark the arrival field as invalid if the departure field is set, but not arrival.
        arrivalDatePicker.pseudoClassStateChanged(INVALID,
                arrivalDatePicker.getPseudoClassStates().contains(INVALID)
                        || (arrivalDatePicker.getValue() == null && departureDatePicker.getValue() != null));
    }

    /**
     * Pop-up the visits-popup for the given Country.
     * @param country The Country to pop up for.
     */
    private void popupVisits(final Country country) {
        popupCountry = country;
        visitsPopup.show(root.getScene().getWindow());
        visitsPopupCountryNameLabel.setText(country.getShortName());
        visitsPopupListView.setItems(createObservableVisitsToCountryList(countryCollector, popupCountry));
    }

    /**
     * Pop-up the visits-popup for the selected Country, if any.
     */
    @FXML
    private void popupVisitsForSelectedCountry() {
        if (inputCountry != null) {
            popupVisits(inputCountry);
        } else if (countriesList.getSelectionModel().getSelectedItem() != null) {
            popupVisits(countriesList.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Set up the visits-popup before first show().
     */
    private void setupVisitsPopup() {
        visitsPopup.setAutoHide(true);
        visitsPopup.getContent().add(visitsPopupRoot);

        // Hide the popup when ESC is pressed, bypassing child listeners
        visitsPopup.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                visitsPopup.hide();
                e.consume();
            }
        });

        // Set up the ListView in the popup
        visitsPopupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        visitsPopupListView.setCellFactory(countryListView -> new ListCell<>() {
            @Override
            protected void updateItem(final Visit item, final boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                } else {
                    if (item.getArrival() == null || item.getDeparture() == null) {
                        setText("Date not specified");
                    } else {
                        final String arrivalText = arrivalDatePicker.getConverter().toString(item.getArrival());
                        final String departureText = departureDatePicker.getConverter().toString(item.getDeparture());
                        setText(String.format("%-10s          to          %-10s", arrivalText, departureText));
                    }
                }
            }
        });
    }
}
