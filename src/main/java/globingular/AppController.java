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

    public void initialize() {
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
    }

    @FXML
    void handleClickMeButtonAction() {
        clickMeButton.setText("Thanks!");
    }
}
