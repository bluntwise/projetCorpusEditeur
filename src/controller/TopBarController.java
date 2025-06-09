package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopBarController {

    public MenuBar menuBar;
    private MainController parent;


    private boolean highlighted = false;
    public void setParent(MainController parent) {
        this.parent = parent;
    }
    public boolean getHighlighted() {
        return highlighted;
    }
    @FXML
    public void handleShowCommonWords() {
        if (parent == null){return;}

        if (!highlighted){
            Set<String> commonsWords = parent.compareTexts(); // ou une autre méthode si tu veux déclencher l'analyse
            displayWithHighlights(commonsWords);
            highlighted = true;

        }else{
            removeHighlights();
            highlighted = false;
        }
    }

    @FXML
    private void onToggleFooterClicked() {
        parent.toggleFooterVisibility(); // `parent` est une référence à MainController
    }


    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @FXML
    public void removeHighlights() {
        parent.getLeftTextArea().setVisible(true);
        parent.getRightTextArea().setVisible(true);
        parent.getLeftTextFlow().setVisible(false);
        parent.getRightTextFlow().setVisible(false);
    }
    @FXML
    public void displayWithHighlights(Set<String> commonsWords){
        parent.getLeftTextArea().setVisible(false);
        parent.getRightTextArea().setVisible(false);
        parent.getLeftTextFlow().setVisible(true);
        parent.getRightTextFlow().setVisible(true);

        String leftText = parent.getLeftTextArea().getText();
        String rightText = parent.getRightTextArea().getText();

        applyWithHighlights(leftText, commonsWords, parent.getLeftTextFlow());
        applyWithHighlights(rightText, commonsWords, parent.getRightTextFlow());

    }

    @FXML
    public void applyWithHighlights(String content, Set<String> commonsWords, TextFlow targetFlow){
        targetFlow.getChildren().clear();

        Pattern pattern = Pattern.compile("\\p{L}+(?:['’-]\\p{L}+)*[\\p{P}]*|\\s+|\\p{P}");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String part = matcher.group();

            Text textNode = new Text(part);

            if (commonsWords.contains(part.trim())) {
                textNode.setFill(Color.RED);
                textNode.setStyle("-fx-font-weight: bold;"); // fluo
            } else {
                textNode.setFill(Color.BLACK); // texte normal
            }

            targetFlow.getChildren().add(textNode);

        }
    }
}
