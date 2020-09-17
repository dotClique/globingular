package globingular;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.util.XMLResourceDescriptor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AppController {

    @FXML
    Button clickMeButton;

    @FXML
    ImageView imgView;

    public ArrayList<String> countryList() {
        ArrayList<String> countries = new ArrayList<>();
        countries.add("NP");
        countries.add("US");
        countries.add("RU");
        countries.add("IR");
        countries.add("AU");
        countries.add("GL");

        return countries;
    }

    public Document createDocument() {
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory file =  new SAXSVGDocumentFactory(parser);
            String uri = "/svg/world-states.svg";
            InputStream stream = getClass().getResourceAsStream("/svg/world-states.svg");
            Document doc = file.createDocument(uri, stream);

            for (String country : countryList()) {
                Element c = doc.getElementById(country);
                c.setAttribute("style", "fill: #7fe5f0");
            }

            return doc;

        } catch (IOException io) {
            io.printStackTrace();

        } return null;
    }

    public void initialize() {
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        TranscoderInput transcoderIn = new TranscoderInput(createDocument());
        try {
            transcoder.transcode(transcoderIn, null);
            Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
            imgView.setImage(img);

        } catch (TranscoderException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleClickMeButtonAction() {
        clickMeButton.setText("Thanks!");
    }
}
