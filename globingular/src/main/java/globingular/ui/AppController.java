package globingular.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
import globingular.json.PersistenceHandler;

public class AppController implements Initializable {

    private PersistenceHandler persistence;
    private CountryCollector countryCollector;

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
