package src;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class that is the Application
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Create an FXMLLoader to load the FXML file describing the main interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));

        // Load the UI hierarchy from the FXML file into a Parent node
        Parent root = loader.load();

        // Create a new Scene with the loaded UI and set its dimensions to 800x400
        Scene scene = new Scene(root, 800, 400);

        // Set the scene to the primary stage (the main window of the application)
        primaryStage.setScene(scene);

        primaryStage.setTitle("Unknown Variable");
        primaryStage.show();
        System.out.println("Loading ...");

    }

    /**
     * Initialize of javaFX motor, create graphic environnement call auto method start
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
