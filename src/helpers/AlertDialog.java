package src.helpers;

import src.controller.AlertDialogController;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertDialog {
    public static void show(String bigTitle, String title, String message) {
        try{
            FXMLLoader loader = new FXMLLoader(AlertDialog.class.getResource("/view/ErrorDialogView.fxml"));
            Parent root = loader.load();

            AlertDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(bigTitle);
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
