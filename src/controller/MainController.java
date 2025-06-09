package src.controller;
import java.io.IOException;
import java.util.Set;

import src.helpers.AlertDialog;
import src.helpers.LevenshteinDistance;
import src.helpers.CompareText;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


/**
 * Main Controller of the Application CorpusEditeur
 * Handle initialisation of the interface with all Controllers declaraation
 */
public class MainController {


    @FXML
    private AnchorPane menuBarContainer;
    @FXML
    private TopBarController menuBarController;


    @FXML private TextArea leftTextArea;
    @FXML private TextArea rightTextArea;

    @FXML private TextFlow leftTextFlow;
    @FXML private TextFlow rightTextFlow;

    private TextPanelController TextPanelControllerLeft;
    private TextPanelController TextPanelControllerRight;

    // Stage used
    private Stage stage;

    @FXML
    private AnchorPane textpanel1;
    @FXML
    private AnchorPane textpanel2;

    @FXML
    private AnchorPane footerContainer;
    private FooterController footerController;


    private boolean isFooterVisible = true;


    /**
     * Display or not the footer with attribute boolean isFooterVisible
     */
    public void toggleFooterVisibility() {
        isFooterVisible = !isFooterVisible;
        footerContainer.setVisible(isFooterVisible);
        footerContainer.setManaged(isFooterVisible); // ajuste la place occupée
    }


    /**
     * Initializes the main user interface by loading various FXML views
     * (menu bar, text panels, footer) and configuring their corresponding controllers.
     *
     */

    public void initialize() {
        try {

            // Load the view FMXL for the top bar
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/TopBarView.fxml"));
            VBox menuNode = menuLoader.load();

            // Get the controller of the top bar
            TopBarController menuCtrl = menuLoader.getController();

            // Manual set
            menuCtrl.setParent(this);

            this.menuBarController = menuCtrl;
            // Injection of the node in the main Interface
            menuBarContainer.getChildren().add(menuNode);

            // load fxml file for left panel
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/TextPanelView.fxml"));

            // Parses the FXML and returns the root Node of the UI component
            Node node1 = loader1.load();

            // Collect the controller associated with the fxml file
            TextPanelController ctrl1 = loader1.getController();

            // set ParentController to communicate with MainController
            ctrl1.setParent(this);

            // Assigns javaFX stage
            ctrl1.setStage(stage);

            // select the side of the panel
            ctrl1.setZone("left");

            // Adds the loaded UI component to the left toolbar container
            textpanel1.getChildren().add(node1);

            // storage of the controller
            this.TextPanelControllerLeft = ctrl1;

            // Same for the right TextPanelController
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/TextPanelView.fxml"));
            Node node2 = loader2.load();
            TextPanelController ctrl2 = loader2.getController();
            ctrl2.setParent(this);
            ctrl2.setStage(stage);
            ctrl2.setZone("right");
            this.TextPanelControllerRight = ctrl2;
            textpanel2.getChildren().add(node2);


            // Set visible because mode commons words disabled
            leftTextArea.setVisible(true);
            leftTextFlow.setVisible(false);
            rightTextArea.setVisible(true);
            rightTextFlow.setVisible(false);


            // Same as TextPanelController for FooterController
            FXMLLoader footerLoader = new FXMLLoader(getClass().getResource("/view/FooterView.fxml"));
            Node footerNode = footerLoader.load();
            FooterController footerCtrl = footerLoader.getController();
            footerContainer.getChildren().add(footerNode);
            this.footerController = footerCtrl;

        } catch (IOException e) {
            AlertDialog.show("Erreur", "Problème", "Error loading FXML file.");
            throw new RuntimeException(e);
        }
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

    /**
     * @return boolean value if commons words mode is enabled
     */
    public boolean getHighlighted(){
        return this.menuBarController.getHighlighted();
    }

    /**
     * Compare TextPanels contents and return commons words
     * @return Set<String> with commons words
     */
    public Set<String> compareTexts() {
        return CompareText.compareTextsUtils(this.getLeftTextArea().getText(), this.getRightTextArea().getText());
    }

    /**
     *
     * @param zone
     * @return TextPanelController depends on zone params
     */
    public TextPanelController getTextPanelController(String zone) {
        if (zone.equals("left")) {
            return this.TextPanelControllerLeft;
        }else if (zone.equals("right")) {
            return this.TextPanelControllerRight;
        }
        return null;
    }


    public TopBarController getMenuBarController() {
        return menuBarController;
    }


    public FooterController getFooterController() {
        return footerController;
    }

    /**
     *
     * @param s
     * @param t
     * @return integer value LevenshteinDistance
     */
    public int getLevenshteinDistance(String s, String t) {
        return LevenshteinDistance.distanceCharLevel(s, t);
    }



}