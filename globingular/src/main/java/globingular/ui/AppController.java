package globingular.ui;

import java.net.URL;
import java.util.*;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;

import org.w3c.dom.Document;

import globingular.core.CountryCollector;
import globingular.json.PersistenceHandler;
import org.w3c.dom.Element;

public class AppController implements Initializable {

    private PersistenceHandler persistence;
    private CountryCollector countryCollector;
    private final Document document = new CreateDocument().createDocument();

    @FXML
    ListView<String> countriesList;

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

        countryCollector.visitedCountriesProperty().addListener((SetChangeListener<? super String>) e -> {
            if (e.wasAdded()) {
                setColor(e.getElementAdded(), Colors.COUNTRY_VISITED);
            } else {
                setColor(e.getElementRemoved(), Colors.COUNTRY_NOT_VISITED);
            }
        });

        countriesList.itemsProperty().set(countryCollector.getVisitedCountriesSorted());
        countriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        updateMap();
    }

    @FXML
    void onCountryAdd() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            countryCollector.setVisited(input);
            countryInput.clear();
        }
        persistence.saveState(countryCollector);
    }

    @FXML
    void onCountryDel() {
        String input = countryInput.getText();
        if (!input.isBlank()) {
            countryCollector.removeVisited(input);
            countryInput.clear();
        } else {
            // Array conversion necessary to prevent the removal of items from foreach-target
            for (Object countryCode : countriesList.getSelectionModel().getSelectedItems().toArray()) {
                countryCollector.removeVisited((String) countryCode);
            }
        }
        countriesList.getSelectionModel().clearSelection();
        persistence.saveState(countryCollector);
    }

    private void setColor(String countryCode, Colors color) {
        Element c = document.getElementById(countryCode);

        if (c == null) {
            return;
        }

        c.setAttribute("style", "fill: " + color.hex);
        updateMap();
    }

    private void setColorAll(Collection<String> countryCodes, Colors color) {
        for (String countryCode : countryCodes) {
            setColor(countryCode, color);
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
