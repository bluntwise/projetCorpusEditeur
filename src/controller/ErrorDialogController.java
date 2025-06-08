package src.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorDialogController {
    @FXML
    private Label errorTitleLabel;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Stage dialogStage;
    @FXML
    private void onClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }



    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public void setError(String title, String message) {
        errorTitleLabel.setText(title);
        errorMessageLabel.setText(message);
    }
}
