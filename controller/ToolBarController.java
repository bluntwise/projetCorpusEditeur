package controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import helpers.ErrorDialog;
import helpers.FileContentRaw;
import helpers.TextParserCorpus;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MainModel;
import javafx.fxml.FXML;

import static helpers.RomanConverter.toRomanWithDot;

public class ToolBarController {
    private MainController parent;
    private String zone;
    private File FileSource;

    @FXML
    private ComboBox<String> comboBoxChapters;

    private MainModel model;
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
        model = new MainModel();
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
            System.out.println("Aucun fichier téléchargé");
            return;
        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateCorpusText();
    }


    @FXML
    public void editFile() {
        TextArea targetTextArea = "left".equals(zone)
                ? parent.getLeftTextArea()
                : parent.getRightTextArea();

        boolean isNowEditable = !targetTextArea.isEditable();
        targetTextArea.setEditable(isNowEditable);

        if (isNowEditable) {
            targetTextArea.setStyle("-fx-control-inner-background: #ffffe0;");

        } else {
            targetTextArea.setStyle("");

            if (parent.getHighlighted()){
                System.out.println(isNowEditable);

                ErrorDialog.show("Problème", "Impossible de modifier le chapitre, l'affichage des mots communs est en cours.");
            }

            int selectedIndex = comboBoxChapters.getSelectionModel().getSelectedIndex();
            model.updateChapter(selectedIndex, targetTextArea.getText());
        }
    }

    public String inverseZone(){
        if (zone == "left") {
            return "right";
        }else{
            return "left";
        }
    }

    public File getFileSource() {
        return FileSource;
    }
    public MainModel getModel() {
        return model;
    }

    public void setModel(MainModel model) {
        this.model = model;
    }



    public void updateCorpusText(){
        ToolBarController otherController = parent.getToolBarController(inverseZone());

        if (otherController != null && otherController.getFileSource() != null &&
                otherController.getFileSource().getAbsolutePath().equals(this.FileSource.getAbsolutePath())) {

            System.out.println("Même fichier détecté dans l'autre zone, rechargement du contenu...");

            try {
                // Lire tout le fichier
                String fileContent = Files.readString(FileSource.toPath());

                // Reparser le fichier
                TextParserCorpus parser = new TextParserCorpus(fileContent);
                MainModel newModel = parser.parse();

                // Mettre à jour le modèle du contrôleur opposé
                otherController.setModel(newModel);

                // Forcer rechargement de l'affichage du chapitre sélectionné
                int selectedIndex = otherController.getComboBoxChapters().getSelectionModel().getSelectedIndex();
                if (selectedIndex == -1) selectedIndex = 0;

                otherController.loadChapter(selectedIndex);

            } catch (IOException e) {
                e.printStackTrace();
                ErrorDialog.show("Erreur", "Impossible de recharger le fichier.");
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
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
            // Tu peux maintenant le lire et l'afficher dans un TextArea
        }

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
            ErrorDialog.show("Problème", "Aucun fichier séléctionné.");
        }


    }


}