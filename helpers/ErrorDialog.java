package helpers;

import controller.ErrorDialogController;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorDialog {
    public static void show(String title, String message) {
        try{
            FXMLLoader loader = new FXMLLoader(ErrorDialog.class.getResource("/view/ErrorDialogView.fxml"));
            Parent root = loader.load();

            ErrorDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Erreur");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(root));

            controller.setDialogStage(dialogStage);
            controller.setError(title, message);

            dialogStage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
