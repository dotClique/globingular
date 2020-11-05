package globingular.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * <p>The App class handles the starting of the Globingular app.
 * A stage is set here, using a preexisting FXML-file.
 * The main method launches a new instance of the application.</p>
 */

public final class App extends Application {

    /**
     * Default window height.
     */
    public static final int WINDOW_HEIGHT = 600;

    /**
     * Default window width.
     */
    public static final int WINDOW_WIDTH = 1290;

    /**
     * Starts the application in this instance.
     * @param primaryStage The Stage this application will fill
     * @throws IOException if FXML-file cannot be found
     */
  @Override
  public void start(final Stage primaryStage) throws IOException {
    final Parent parent = FXMLLoader.load(getClass().getResource("/fxml/App.fxml"));
    primaryStage.setScene(new Scene(parent, WINDOW_WIDTH, WINDOW_HEIGHT));
    primaryStage.show();
    primaryStage.setTitle("Globingular");
    primaryStage.getIcons().add(new Image("/icons/globingular.png"));
    }

    /**
     * Main method, launches a new instance of the application.
     * @param args The arguments to pass to the program
     */
  public static void main(final String[] args) {
    launch(args);
  }
}
