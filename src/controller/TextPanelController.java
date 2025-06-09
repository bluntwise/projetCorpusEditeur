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

public class TextPanelController {
    private MainController parent;
    private String zone;
    private File FileSource;
    private boolean isNowEditable = false;
    @FXML
    private ComboBox<String> comboBoxChapters;

    private CorpusModel model;
    private Stage stage;

    public void setParent(MainController parent) {
        this.parent = parent;
        this.FileSource = null;
        setupComboBoxChapters();
    }

    public void setZone(String zone) {
        this.zone = zone;
    }


    @FXML
    public void initialize() {
        model = new CorpusModel();
    }


    public void setupComboBoxChapters() {
        comboBoxChapters.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null ) {
                loadChapter(comboBoxChapters.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void loadChapter(int chapterIndex) {
        String content = model.getAllContent().get(chapterIndex);

        // Mise à jour de la zone de texte
        if ("left".equals(zone)) {
            parent.getLeftTextArea().setText(content);

            Text textNode = new Text(content);
            parent.getLeftTextFlow().getChildren().setAll(textNode);
            if (parent.getHighlighted()){
                parent.getMenuBarController().setHighlighted(false);
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
        parent.getFooterController().setCommonWordsCount(parent.compareTexts().size());
        parent.getFooterController().setLevenshteinDistance(parent.getLevenshteinDistance(parent.getTextLeftArea(), parent.getTextRightArea()));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void saveFile(){
        if (this.FileSource == null) {
            AlertDialog.show("Erreur","Problème", "Aucun fichier téléchargé");
            return;
        }

        if (parent.getHighlighted()){
            AlertDialog.show("Erreur","Problème", "Affichage des mots en communs en cours, action impossible.");
            return;
        }

        if (!isNowEditable){
            LinkedHashMap<Integer, String> allContent = model.getAllContent();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FileSource))) {
                for(Map.Entry<Integer, String> entry : allContent.entrySet()) {
                    Integer id_chapter = entry.getKey();
                    String content_chapter = entry.getValue();


                    writer.write("\n" + toRomanWithDot(id_chapter + 1) + ".\n"); // chapitre 1 = I.
                    writer.newLine();

                    // Puis écrire toutes les lignes
                    writer.write(content_chapter);
                    writer.newLine();
                }

                AlertDialog.show("Succès", "Validation", "Le fichier a été enregistré.");
            } catch (IOException e) {
                AlertDialog.show("Erreur", "Inconnue", "Une erreur est survenue.");
            }
            updateCorpusText();
        }else{
            AlertDialog.show("Problème","Erreur", "Veuillez finir la modification du fichier en réappuyant sur le bouton Modification.");
        }

    }


    @FXML
    public void editFile() {
        TextArea targetTextArea = "left".equals(zone)
                ? parent.getLeftTextArea()
                : parent.getRightTextArea();

        if (parent.getHighlighted()){
            AlertDialog.show("Erreur", "Problème", "Impossible de modifier le chapitre, l'affichage des mots communs est en cours.");
            return;
        }
        isNowEditable = !targetTextArea.isEditable();
        targetTextArea.setEditable(isNowEditable);

        if (isNowEditable) {
            targetTextArea.setStyle("-fx-control-inner-background: #ffffe0;");

        } else {
            targetTextArea.setStyle("");
            int selectedIndex = comboBoxChapters.getSelectionModel().getSelectedIndex();
            model.updateChapter(selectedIndex, targetTextArea.getText());
        }
    }

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
    public CorpusModel getModel() {
        return model;
    }

    public void setModel(CorpusModel model) {
        this.model = model;
    }



    public void updateCorpusText(){
        TextPanelController otherController = parent.getToolBarController(inverseZone());

        if (otherController != null && otherController.getFileSource() != null &&
                otherController.getFileSource().getAbsolutePath().equals(this.FileSource.getAbsolutePath())) {

            System.out.println("Même fichier détecté dans l'autre zone, rechargement du contenu...");

            try {
                // Lire tout le fichier
                String fileContent = Files.readString(FileSource.toPath());

                // Reparser le fichier
                TextParserCorpus parser = new TextParserCorpus(fileContent);
                CorpusModel newModel = parser.parse();

                // Mettre à jour le modèle du contrôleur opposé
                otherController.setModel(newModel);

                // Forcer rechargement de l'affichage du chapitre sélectionné
                int selectedIndex = otherController.getComboBoxChapters().getSelectionModel().getSelectedIndex();
                if (selectedIndex == -1) selectedIndex = 0;

                otherController.loadChapter(selectedIndex);

            } catch (IOException e) {
                AlertDialog.show("Erreur","Problème", "Impossible de recharger le fichier.");
            }
        }
    }

    public ComboBox<String> getComboBoxChapters() {
        return comboBoxChapters;
    }

    @FXML
    private void chargeFile(){

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
            model = parser.parse();
            int nbChapters = model.getIdChapter();

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