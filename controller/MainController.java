package controller;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import helpers.FileContentRaw;
import helpers.TextParserCorpus;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MainModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {


    @FXML
    private AnchorPane menuBarContainer;
    @FXML private TextArea leftTextArea;
    @FXML private TextArea rightTextArea;

    @FXML private TextFlow leftTextFlow;
    @FXML private TextFlow rightTextFlow;


    private Stage stage;

    @FXML
    private AnchorPane toolbar1;

    @FXML
    private AnchorPane toolbar2;


    public void initialize() {
        try {

            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/MenuBarView.fxml"));
            VBox menuNode = menuLoader.load();
            MenuBarController menuCtrl = menuLoader.getController();
            menuCtrl.setParent(this);

            menuBarContainer.getChildren().add(menuNode);

            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/ToolBarView.fxml"));
            Node node1 = loader1.load();
            ToolBarController ctrl1 = loader1.getController();
            ctrl1.setParent(this);
            ctrl1.setStage(stage);
            ctrl1.setZone("left");
            toolbar1.getChildren().add(node1);

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/ToolBarView.fxml"));
            Node node2 = loader2.load();
            ToolBarController ctrl2 = loader2.getController();
            ctrl2.setParent(this);
            ctrl2.setStage(stage);
            ctrl2.setZone("right");
            toolbar2.getChildren().add(node2);

            leftTextArea.setVisible(true);
            leftTextFlow.setVisible(false);
            rightTextArea.setVisible(true);
            rightTextFlow.setVisible(false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTextLeftArea(){
        return leftTextArea.getText();
    }

    public String getTextRightArea(){
        return rightTextArea.getText();
    }

    public TextArea getLeftTextArea() {
        return leftTextArea;
    }

    public Set<String> compareTexts() {
        String[] words1 = getLeftTextArea().getText().split("\\s+");
        String[] words2 = getRightTextArea().getText().split("\\s+");

        Set<String> set1 = new HashSet<>(List.of(words1));
        Set<String> set2 = new HashSet<>(List.of(words2));

        // Intersection : mots communs
        Set<String> commonWords = new HashSet<>(set1);
        commonWords.retainAll(set2);

        System.out.println("Mots en commun (" + commonWords.size() + ") : " + commonWords);
        return commonWords;
    }



    public TextArea getRightTextArea() {
        return rightTextArea;
    }



    public Stage getStage() {
        return stage;
    }
}