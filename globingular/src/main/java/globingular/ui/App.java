package globingular.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class App extends Application {

    /**
     * Starts the application in this instance.
     * @param primaryStage The Stage this application will fill
     * @throws IOException if FXML-file cannot be found
     */
  @Override
  public void start(final Stage primaryStage) throws IOException {
    final Parent parent = FXMLLoader.load(getClass().getResource("/fxml/App.fxml"));
    primaryStage.setScene(new Scene(parent));
    primaryStage.show();
  }

    /**
     * Main method, launches a new instance of the application.
     * @param args The arguments to pass to the program
     */
  public static void main(final String[] args) {
    launch(args);
  }
}
