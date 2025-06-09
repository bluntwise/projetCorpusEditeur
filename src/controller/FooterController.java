package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller of the Application Footer
 */
public class FooterController {

    @FXML
    private Label levenshteinValueLabel;

    @FXML
    private Label commonWordsValueLabel;

    public void setLevenshteinDistance(int distance) {
        levenshteinValueLabel.setText(String.valueOf(distance));
    }

    public void setCommonWordsCount(int count) {
        commonWordsValueLabel.setText(String.valueOf(count));
    }

}
