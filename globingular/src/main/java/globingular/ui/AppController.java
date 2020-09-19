package globingular.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.Property;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

import javafx.util.Callback;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;

import org.w3c.dom.Document;

import globingular.core.CountryCollector;
import globingular.json.PersistenceHandler;
import org.w3c.dom.Element;

public class AppController implements Initializable {

    private PersistenceHandler persistence;
    private CountryCollector countryCollector;
    private Document document = new CreateDocument().createDocument();


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

        // TODO: list doesn't refresh on add/remove.
        // TODO: newly added items unselectable.
        // TODO: removing doesn't exactly work.
        countriesList.itemsProperty().set(
                new ModifiableObservableListBase<>() {
                    @Override
                    public String get(int index) {
                        return countryCollector.getVisitedCountries()[index];
                    }

                    @Override
                    public int size() {
                        return countryCollector.getVisitedCountries().length;
                    }

                    @Override
                    protected void doAdd(int index, String element) {
                        countryCollector.setVisited(element);
                    }

                    @Override
                    protected String doSet(int index, String element) {
                        doAdd(0, element);
                        return null;
                    }

                    @Override
                    protected String doRemove(int index) {
                        countryCollector.removeVisited(get(index));
                        return null;
                    }
                });

        updateMap();

        document.getElementById("NO").setAttribute("style","fill: #ff00ff");

        countryCollector.visitsProperty().addListener((SetChangeListener<? super String>) e -> {
            if (e.wasAdded()) {
                setColor(e.getElementAdded(), Colors.COUNTRY_VISITED);
            } else {
                removeColor(e.getElementRemoved());
            }
        });

    }

//    private void updateView() {
//        countriesList.getItems().clear();
//        countriesList.getItems().addAll(List.of(countryCollector.getVisitedCountries()));
//    }

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
        }
        persistence.saveState(countryCollector);
    }

    private void setColor(String countryCode, Colors color) {
        Element c = document.getElementById(countryCode);

        if (c == null) {
            throw new RuntimeException("Country not on map: " + countryCode);
        }

        c.setAttribute("style", "fill: " + color.hex);
        updateMap();
    }

    private void removeColor(String countryCode) {
        Element c = document.getElementById(countryCode);

        if (c == null) {
            throw new RuntimeException("Country not on map: " + countryCode);
        }

        c.setAttribute("style", "");
        updateMap();
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
