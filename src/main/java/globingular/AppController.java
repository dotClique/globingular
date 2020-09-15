package globingular;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;


import java.io.IOException;
import java.io.InputStream;

public class AppController {
    
    @FXML
    Button clickMeButton;

    @FXML
    ImageView imgView;

    public void initialize() {
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        try (InputStream file = getClass().getResourceAsStream("https://raw.githubusercontent.com/raphaellepuschitz/SVG-World-Map/master/src/world-states.svg")) {
            TranscoderInput transcoderIn = new TranscoderInput(file);
            try {
                transcoder.transcode(transcoderIn, null);
                Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
                imgView.setImage(img);

            } catch (TranscoderException e) {
                e.printStackTrace();
            }
        }
        catch (IOException io){
            io.printStackTrace();
        }

    }

    @FXML
    void handleClickMeButtonAction() {
        clickMeButton.setText("Thanks!");
    }
}
