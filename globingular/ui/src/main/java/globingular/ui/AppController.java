package globingular.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import globingular.persistence.PersistenceHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;

import org.w3c.dom.Document;

import globingular.core.CountryCollector;

public class AppController implements Initializable {

    /**
     * Responsible for persisting state across runs.
     */
    private PersistenceHandler persistence;
    /**
     * Manager of which countries exist and have been visited;
     * core component storing all state.
     */
    private CountryCollector countryCollector;

    /**
     * The list of countries visible in the interface.
     */
    @FXML
    private ListView<String> countriesList;

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
     * Initialize the controller on load.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        persistence = new PersistenceHandler();
        countryCollector = persistence.loadState();

        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        Document document = new CreateDocument().createDocument();
        TranscoderInput transcoderIn = new TranscoderInput(document);
        try {
            transcoder.transcode(transcoderIn, null);
            Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
            imgView.setImage(img);

        } catch (TranscoderException e) {
            e.printStackTrace();
        }

        updateView();
    }

    /**
     * Update the list of countries on addition of visited Country.
     */
    private void updateView() {
        countriesList.getItems().clear();
        countriesList.getItems().addAll(List.of(countryCollector.getVisitedCountries()));
    }

    /**
     * Add the inputted country-code to list of visited countries.
     */
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

    /**
     * Remove the inputted country-code from list of visited countries.
     */
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

    @FXML
    public void normalizeInput() {

    }


}
