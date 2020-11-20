package globingular.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class AppIT extends ApplicationTest {

    private AppController controller;

    @Override
    public void start(final Stage primaryStage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AppIT.fxml"));
        final Parent parent = loader.load();
        this.controller = loader.getController();
        primaryStage.setScene(new Scene(parent, 1300, 600));
        primaryStage.show();
    }

    @BeforeEach
    public void setupItems() throws URISyntaxException {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("/json/sampleCollector.json"))) {
            String port = System.getProperty("globingular.port");
            assertNotNull(port, "No globingular.port system property set");
            URI baseUri = new URI("http://localhost:" + port + "/globingular/");
            System.out.println("Base URI: " + baseUri);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testController_initial() {
        assertNotNull(this.controller);
    }
}
