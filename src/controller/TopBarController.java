package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Controller of the TopBar
 */
public class TopBarController {

    public MenuBar menuBar;
    private MainController parent;

    // value to know if common words enabled
    private boolean highlighted = false;

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    public boolean getHighlighted() {
        return highlighted;
    }


    /**
     * Method that enabled common words or disabled
     */
    @FXML
    public void handleShowCommonWords() {
        if (parent == null){return;}

        // collect and display common words
        if (!highlighted){
            Set<String> commonWords = parent.compareTexts();
            displayWithHighlights(commonWords);
            highlighted = true;

        }else{
            removeHighlights();
            highlighted = false;
        }
    }

    /**
     * Footer visible or not (call parent method)
     */
    @FXML
    private void onToggleFooterClicked() {
        parent.toggleFooterVisibility();
    }



    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }


    /**
     * Set visible textArea because common Words mode disabled
     */
    @FXML
    public void removeHighlights() {
        parent.getLeftTextArea().setVisible(true);
        parent.getRightTextArea().setVisible(true);
        parent.getLeftTextFlow().setVisible(false);
        parent.getRightTextFlow().setVisible(false);
    }

    /**
     * Display common words with highlighted style
     * @param commonsWords
     */
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

    /**
     * Technical method that applu highlights on text parameters
     * @param content
     * @param commonsWords
     * @param targetFlow
     */
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
                textNode.setStyle("-fx-font-weight: bold;");
            } else {
                textNode.setFill(Color.BLACK);
            }

            targetFlow.getChildren().add(textNode);

        }
    }
}
