package controller;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import helpers.LevenshteinDistance;
import helpers.CompareText;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {


    @FXML
    private AnchorPane menuBarContainer;
    @FXML
    private MenuBarController menuBarController;

    @FXML private TextArea leftTextArea;
    @FXML private TextArea rightTextArea;

    @FXML private TextFlow leftTextFlow;
    @FXML private TextFlow rightTextFlow;
    private ToolBarController toolBarControllerLeft;
    private ToolBarController toolBarControllerRight;

    private Stage stage;

    @FXML
    private AnchorPane toolbar1;
    @FXML
    private AnchorPane toolbar2;

    @FXML
    private AnchorPane footerContainer;
    private FooterController footerController;

    public void initialize() {
        try {

            // Load the view FMXL for the top bar
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/MenuBarView.fxml"));
            VBox menuNode = menuLoader.load();
            // Get the controller of the top bar
            MenuBarController menuCtrl = menuLoader.getController();

            // Manual set
            menuCtrl.setParent(this);

            this.menuBarController = menuCtrl;
            // Injection of the node in the main Interface
            menuBarContainer.getChildren().add(menuNode);


            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/ToolBarView.fxml"));
            Node node1 = loader1.load();
            ToolBarController ctrl1 = loader1.getController();
            ctrl1.setParent(this);
            ctrl1.setStage(stage);
            ctrl1.setZone("left");
            toolbar1.getChildren().add(node1);
            this.toolBarControllerLeft = ctrl1;

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/ToolBarView.fxml"));
            Node node2 = loader2.load();
            ToolBarController ctrl2 = loader2.getController();
            ctrl2.setParent(this);
            ctrl2.setStage(stage);
            ctrl2.setZone("right");
            this.toolBarControllerRight = ctrl2;
            toolbar2.getChildren().add(node2);

            leftTextArea.setVisible(true);
            leftTextFlow.setVisible(false);
            rightTextArea.setVisible(true);
            rightTextFlow.setVisible(false);

            FXMLLoader footerLoader = new FXMLLoader(getClass().getResource("/view/FooterView.fxml"));
            Node footerNode = footerLoader.load();
            FooterController footerCtrl = footerLoader.getController();
            footerContainer.getChildren().add(footerNode);
            this.footerController = footerCtrl;

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

    public TextArea getRightTextArea() {
        return rightTextArea;
    }
    public TextFlow getLeftTextFlow() {
        return leftTextFlow;
    }

    public TextFlow getRightTextFlow() {
        return rightTextFlow;
    }

    public boolean getHighlighted(){
        return this.menuBarController.getHighlighted();
    }

    public Set<String> compareTexts() {
        return CompareText.compareTextsUtils(this.getTextLeftArea(), this.getTextRightArea());
    }

    public MenuBarController getMenuBarController() {
        return menuBarController;
    }


    public FooterController getFooterController() {
        return footerController;
    }

    public int getLevenshteinDistance(String s, String t) {
        return LevenshteinDistance.distanceCharLevel(s, t);
    }



}