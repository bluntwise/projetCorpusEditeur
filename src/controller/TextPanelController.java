package src.controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import src.helpers.AlertDialog;
import src.helpers.FileContentRaw;
import src.helpers.TextParserCorpus;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import src.model.CorpusModel;
import javafx.fxml.FXML;

import static src.helpers.RomanConverter.toRomanWithDot;


/**
 * Controller for a representation of a text editor and allows
 * you to perform various actions on text files
 */
public class TextPanelController {


    private MainController parent;

    // left or right
    private String zone;

    // File used in this TextPanelController
    private File FileSource = null;

    // if we can edit this corpus or not
    private boolean isNowEditable = false;

    // menu of chapters
    @FXML
    private ComboBox<String> comboBoxChapters;

    // model Used for this controller
    private CorpusModel model;

    // stage used
    private Stage stage;

    public void setParent(MainController parent) {
        this.parent = parent;
        setupComboBoxChapters();
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    // Create a new model instance and initialize it when the controller is loaded
    @FXML
    public void initialize() {
        model = new CorpusModel();
    }


    /**
     * Setup the menu of chapters with a listener
     */
    public void setupComboBoxChapters() {
        comboBoxChapters.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null ) {
                loadChapter(comboBoxChapters.getSelectionModel().getSelectedIndex());
            }
        });
    }

    /*
     * Load the chapter asked in the textPanel
     * @param chapterIndex
     */
    private void loadChapter(int chapterIndex) {
        String content = model.getAllContent().get(chapterIndex);


        // Check if the current zone is "left"
        if ("left".equals(zone)) {

            // Set the content of the left text area in the main view
            parent.getLeftTextArea().setText(content);

            // Create a new Text node with the given content
            Text textNode = new Text(content);

            // Replace the contents of the left TextFlow with the new text node
            parent.getLeftTextFlow().getChildren().setAll(textNode);

            // If highlighting was previously enabled
            if (parent.getHighlighted()) {

                // Disable highlighting flag in the menu bar controller
                parent.getMenuBarController().setHighlighted(false);

                // Trigger the action to update the display of common words
                parent.getMenuBarController().handleShowCommonWords();
            }

        } else if ("right".equals(zone)) {
            parent.getRightTextArea().setText(content);

            Text textNode = new Text(content);
            parent.getRightTextFlow().getChildren().setAll(textNode);
            if (parent.getHighlighted()){
                parent.getMenuBarController().setHighlighted(false);
                parent.getMenuBarController().handleShowCommonWords();
            }
        }

        // update commons words and Levenshtein distance
        parent.getFooterController().setCommonWordsCount(parent.compareTexts().size());
        parent.getFooterController().setLevenshteinDistance(parent.getLevenshteinDistance(parent.getLeftTextArea().getText(), parent.getRightTextArea().getText()));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    /**
     * Allow to SaveFile with modifications or not
     */
    @FXML
    public void saveFile(){

        // Error if they is any file selected
        if (this.FileSource == null) {
            AlertDialog.show("Erreur","Problème", "Aucun fichier téléchargé");
            return;
        }

        // if commons words mode enabled, forbidden to saveFile
        if (parent.getHighlighted()){
            AlertDialog.show("Erreur","Problème", "Affichage des mots en communs en cours, action impossible.");
            return;
        }

        // if the textArea is not currently editable
        if (!isNowEditable){


            LinkedHashMap<Integer, String> allContent = model.getAllContent();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FileSource))) {
                for(Map.Entry<Integer, String> entry : allContent.entrySet()) {
                    Integer id_chapter = entry.getKey();
                    String content_chapter = entry.getValue();


                    writer.write("\n" + toRomanWithDot(id_chapter + 1) + "\n"); // chapter 1 = I.
                    writer.newLine();

                    // write all lines
                    writer.write(content_chapter);
                    writer.newLine();
                }

                // popup Succes
                AlertDialog.show("Succès", "Validation", "Le fichier a été enregistré.");
            } catch (IOException e) {
                AlertDialog.show("Erreur", "Inconnue", "Une erreur est survenue.");
            }

            // if same file, current modification visible in the other side
            updateCorpusText();
        }else{

            // popup Error
            AlertDialog.show("Problème","Erreur", "Veuillez finir la modification du fichier en réappuyant sur le bouton Modification.");
        }

    }

    /**
     * Allow to edit the corpus with a system of replacement
     * with TextArea and TextFlow
     */
    @FXML
    public void editFile() {

        // Collect the right content
        TextArea targetTextArea = "left".equals(zone)
                ? parent.getLeftTextArea()
                : parent.getRightTextArea();

        // if commons words mode enabled
        if (parent.getHighlighted()){
            AlertDialog.show("Erreur", "Problème", "Impossible de modifier le chapitre, l'affichage des mots communs est en cours.");
            return;
        }

        // Is editable right now
        isNowEditable = !targetTextArea.isEditable();
        targetTextArea.setEditable(isNowEditable);


        if (isNowEditable) {
            // set the background in different color
            targetTextArea.setStyle("-fx-control-inner-background: #ffffe0;");

        } else {

            // Update of the content and style reinitialized
            targetTextArea.setStyle("");
            int selectedIndex = comboBoxChapters.getSelectionModel().getSelectedIndex();
            model.updateChapter(selectedIndex, targetTextArea.getText());
        }
    }

    /**
     * used for updateCorpusText
     * @return side inversed
     */
    public String inverseZone(){
        if (zone.equals("left")) {
            return "right";
        }else{
            return "left";
        }
    }

    public File getFileSource() {
        return FileSource;
    }


    public void setModel(CorpusModel model) {
        this.model = model;
    }


    /**
     * Method to immediately set the update visible in the other side
     * if it's same File
     */
    public void updateCorpusText(){
        // get the other controller
        TextPanelController otherController = parent.getTextPanelController(inverseZone());

        // verification if it's same File
        if (otherController != null && otherController.getFileSource() != null &&
                otherController.getFileSource().getAbsolutePath().equals(this.FileSource.getAbsolutePath())) {


            try {
                // Read all the content file
                String fileContent = Files.readString(FileSource.toPath());

                // Parse again the file
                TextParserCorpus parser = new TextParserCorpus(fileContent);
                CorpusModel newModel = parser.parse();

                // Update the other model
                otherController.setModel(newModel);

                // set the right index
                int selectedIndex = otherController.getComboBoxChapters().getSelectionModel().getSelectedIndex();
                if (selectedIndex == -1) selectedIndex = 0;

                // of the new chapter
                otherController.loadChapter(selectedIndex);

            } catch (IOException e) {
                AlertDialog.show("Erreur","Problème", "Impossible de recharger le fichier.");
            }
        }
    }

    public ComboBox<String> getComboBoxChapters() {
        return comboBoxChapters;
    }

    /**
     * Important Method that allow to charge File on the PC and set visible
     * the File content in the Application
     */
    @FXML
    private void chargeFile(){

        // interface to choose the file to display
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger un fichier texte");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        this.FileSource = selectedFile;


        if (selectedFile != null) {


            FileContentRaw extractContent = new FileContentRaw(selectedFile.getAbsolutePath());
            TextParserCorpus parser = new TextParserCorpus(extractContent.getContent());

            // parse of the content and get a model
            model = parser.parse();
            int nbChapters = model.getIdChapter();

            // setup of the comboBox with Chapters Labels
            List<String> chapterLabels = new ArrayList<>();
            for (int i = 0; i < nbChapters; i++) {
                chapterLabels.add("Chapitre " + (i + 1));
            }

            comboBoxChapters.getItems().setAll(chapterLabels);
            comboBoxChapters.getSelectionModel().selectFirst();
        }else{
            AlertDialog.show("Erreur","Problème", "Aucun fichier séléctionné.");
        }


    }


}